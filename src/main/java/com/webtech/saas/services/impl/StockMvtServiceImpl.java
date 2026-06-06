package com.webtech.saas.services.impl;

import com.webtech.saas.common.PageResponse;
import com.webtech.saas.entities.Product;
import com.webtech.saas.entities.StockMvt;
import com.webtech.saas.mappers.StockMvtMapper;
import com.webtech.saas.repositories.ProductRepository;
import com.webtech.saas.repositories.StockMvtRepository;
import com.webtech.saas.requests.StockMvtRequest;
import com.webtech.saas.responses.StockMvtResponse;
import com.webtech.saas.services.StockMvtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMvtServiceImpl implements StockMvtService {
    private final StockMvtRepository stockMvtRepository;
    private final StockMvtMapper stockMvtMapper;
    private final ProductRepository productRepository;

    @Override
    public void create(StockMvtRequest request) {
        checkIfProductExistById(request.getProductId());

        final StockMvt stockMvt = this.stockMvtMapper.toEntity(request);
        this.stockMvtRepository.save(stockMvt);
    }

    @Override
    public void update(String Id, StockMvtRequest request) {
        final Optional<StockMvt> stockMvtExisting = this.stockMvtRepository.findById(Id);
        if(stockMvtExisting.isEmpty()) {
            log.debug("Stockmvt does exist");
            throw  new EntityNotFoundException("StockMvt does not exist");
        }

        //check if product exist
        checkIfProductExistById(request.getProductId());

        final StockMvt stockMvtToUpdate = this.stockMvtMapper.toEntity(request);
        stockMvtToUpdate.setId(Id);
        this.stockMvtRepository.save(stockMvtToUpdate);
    }

    @Override
    public PageResponse<StockMvtResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<StockMvt> stockMvts = this.stockMvtRepository.findAll(pageRequest);
        final Page<StockMvtResponse> stockMvtsResponse = stockMvts.map(this.stockMvtMapper::toResponse);
        return PageResponse.of(stockMvtsResponse);
    }

    @Override
    public StockMvtResponse findById(String Id) {
        return this.stockMvtRepository.findById(Id)
                .map(this.stockMvtMapper::toResponse)
                .orElseThrow(()-> new EntityNotFoundException("StockMvt wit the id: " + Id + "does not exist"));
    }

    @Override
    public void delete(String id) {
        final StockMvt stockMvt = this.stockMvtRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("StockMvt does exist"));

        this.stockMvtRepository.delete(stockMvt);
    }

    private void checkIfProductExistById(String productId) {
        Optional<Product> product = this.productRepository.findById(productId);
        if (product.isEmpty()) {
            log.debug("Product does not exist");
            throw new EntityNotFoundException("product does not Exist");
        }
    }
}
