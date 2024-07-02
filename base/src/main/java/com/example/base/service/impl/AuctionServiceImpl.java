package com.example.base.service.impl;

import com.example.base.service.AuctionService;
import com.example.common.entity.Auction;
import com.example.common.entity.Status;
import com.example.common.repo.AuctionRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.common.entity.Status.*;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepo repo;
    private static final Logger logger = LoggerFactory.getLogger(AuctionServiceImpl.class);

    @Override
    @Transactional
    public List<Auction> getAllAuctions() {
        return repo.getAllAuctions();
    }

    @Override
    @Transactional
    public Auction getAuction(Integer id) {
        return repo.getAuction(id);
    }

    @Override
    @Transactional
    public Auction saveAuction(Auction auction) {
        if (repo.existsByName(auction.getName())) {
            throw new IllegalArgumentException("Auction with such name already exists");
        }
        auction.setStatusChangeTime(LocalDateTime.now());
        repo.save(auction);
        return auction;
    }

    @Override
    @Transactional
    public Auction deleteAuction(Integer id) {
        return repo.delete(id);
    }

    @Override
    @Transactional
    public List<Auction> getAuctionsByStatus(String status) {
        return repo.findByStatus(Status.valueOf(status));
    }

    @Transactional
    public void processAuction() {
        List<Auction> auctions = getAllAuctions();
        for (Auction auction : auctions) {
            if (auction.getStatus().equals(DONE))
                continue;
            logger.info("Auction process start.");
            if (auction.getStatus().equals(INIT)) {
                auction.setStatus(IN_PROGRESS);
            } else if (auction.getStatus().equals(IN_PROGRESS)) {
                auction.setStatus(DONE);
            }
            auction.setStatusChangeTime(LocalDateTime.now());
            repo.save(auction);
            logger.info("Auction processing completed for auction with id {}", auction.getId());
        }

    }
}
