package com.webtech.saas.requests;

import com.webtech.saas.entities.Category;
import lombok.*;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class productRequest {
    private String name;

    private String reference;

    private String description;

    private Integer alertThreshold;

    private BigDecimal price;

    private String categoryId;
}
