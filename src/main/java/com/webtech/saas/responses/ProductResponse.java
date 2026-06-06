package com.webtech.saas.responses;

import com.webtech.saas.entities.Category;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String Id;

    private String name;

    private String reference;

    private String description;

    private Integer alertThreshold;

    private BigDecimal price;

    private String categoryName;

    private int availableQuantity;
}
