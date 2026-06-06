package com.webtech.saas.requests;

import lombok.*;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;

    private String reference;

    private String description;

    private Integer alertThreshold;

    private BigDecimal price;

    private String categoryId;
}
