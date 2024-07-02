package com.example.base.controller;

import com.example.base.service.AuctionService;
import com.example.common.entity.Auction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auction")
public class AuctionController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping
    public ResponseEntity<List<Auction>> getAuctions() {
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @GetMapping("{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable Integer id) {
        return ResponseEntity.ok(auctionService.getAuction(id));
    }

    @PostMapping
    public ResponseEntity<Auction> createAuction(@Valid @RequestBody Auction auction) {
        return ResponseEntity.ok(auctionService.saveAuction(auction));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Auction> deleteAuction(@PathVariable Integer id) {
        return ResponseEntity.ok(auctionService.deleteAuction(id));
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<Auction>> getAuctionsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(auctionService.getAuctionsByStatus(status));
    }

    @Scheduled(fixedRate = 7200000)
    public void scheduledAuctions() {
        auctionService.processAuction();
    }
}
