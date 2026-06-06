package com.webtech.saas.controllers;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.requests.StockMvtRequest;
import com.webtech.saas.responses.StockMvtResponse;
import com.webtech.saas.services.StockMvtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/stockmvts")
@RequiredArgsConstructor
@Slf4j
public class StockMvtController {
    private final StockMvtService stockMvtService;

    @PostMapping
    public ResponseEntity<Void> createStockMvt(
            @Valid @RequestBody StockMvtRequest stockMvtRequest) {
        this.stockMvtService.create(stockMvtRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<StockMvtResponse>> findAllStockMvt(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(this.stockMvtService.findAll(page, size));
    }

    @GetMapping("/{stockMvt-id}")
    public ResponseEntity<StockMvtResponse> findById(
            @RequestParam("stockMvt-id") String stockMvtId) {
        return ResponseEntity.ok(this.stockMvtService.findById(stockMvtId));
    }

    @PutMapping("/{stockMvt-id}")
    public ResponseEntity<Void> updateStockMvt(
            @Valid @RequestBody StockMvtRequest stockMvtRequest,
            @RequestParam("stockMvt-id") String stockMvtId) {
        this.stockMvtService.update(stockMvtId, stockMvtRequest);
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/{stockMvt-id}")
    public ResponseEntity<Void> deleteStockMvt(
            @RequestParam("stockMvt-id") String stockMvtId) {
        this.stockMvtService.delete(stockMvtId);
        return ResponseEntity.ok().build();
    }
}
