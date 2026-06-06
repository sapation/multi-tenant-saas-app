package com.webtech.saas.requests;

import com.webtech.saas.entities.Product;
import com.webtech.saas.entities.TypeMvt;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMvtRequest {
    private TypeMvt typeMvt;

    private Integer quantity;

    private LocalDate dateMvt;

    private String comment;

    private String productId;
}
