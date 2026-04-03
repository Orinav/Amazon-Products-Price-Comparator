package com.amazori.dto;

public record AmazonApiResponse(
        String status,
        ProductPriceResponse data
) {
}