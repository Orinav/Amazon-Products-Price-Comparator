package com.amazori.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductPriceResponse(
        String asin,

        @JsonProperty("product_title")
        String productTitle,

        @JsonProperty("product_price")
        String price,

        String currency,
        String country
)
{
}