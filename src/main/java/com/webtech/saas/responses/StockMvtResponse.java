package com.webtech.saas.responses;

import com.webtech.saas.entities.Product;
import com.webtech.saas.entities.TypeMvt;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMvtResponse {
    private String Id;

    private TypeMvt typeMvt;

    private Integer quantity;

    private LocalDate dateMvt;

    private String comment;

    private Product product;
}
