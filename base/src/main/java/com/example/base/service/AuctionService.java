package com.example.base.service;

import com.example.common.entity.Auction;

import java.util.List;

public interface AuctionService {
    Auction getAuction(Integer id);

    Auction saveAuction(Auction auction);

    Auction deleteAuction(Integer id);

    List<Auction> getAuctionsByStatus(String status);

    List<Auction> getAllAuctions();

    void processAuction();
}
