package com.webtech.saas.mappers;

import com.webtech.saas.entities.Product;
import com.webtech.saas.entities.StockMvt;
import com.webtech.saas.requests.StockMvtRequest;
import com.webtech.saas.responses.StockMvtResponse;

public class StockMvtMapper {

    public StockMvt toEntity(final StockMvtRequest request) {
        return StockMvt.builder()
                .typeMvt(request.getTypeMvt())
                .quantity(request.getQuantity())
                .dateMvt(request.getDateMvt())
                .comment(request.getComment())
                .product(Product.builder()
                        .Id(request.getProductId())
                        .build())
                .build();
    }

    public StockMvtResponse toResponse(final StockMvt stockMvt) {
        return StockMvtResponse.builder()
                .Id(stockMvt.getId())
                .typeMvt(stockMvt.getTypeMvt())
                .quantity(stockMvt.getQuantity())
                .dateMvt(stockMvt.getDateMvt())
                .comment(stockMvt.getComment())
                .product(stockMvt.getProduct())
                .build();
    }
}
