package com.amazori.controller;

import com.amazori.service.AmazonScraperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Designates this class as a REST Controller.
@RestController

// Sets the base URL path for all endpoints in this controller
@RequestMapping("/api/products")
public class ProductController
{

    private final AmazonScraperService amazonScraperService;

    // Constructor Injection: Wires the Service (Chef) into the Controller (Waiter)
    public ProductController(AmazonScraperService amazonScraperService)
    {
        this.amazonScraperService = amazonScraperService;
    }

    // Handles HTTP GET requests at the specific path "/api/products/details"
    @GetMapping("/details")
    // Extracts the "asin" parameter from the URL query string
    // Extracts the "country" parameter, defaulting to "US" if not provided by the user
    public String getProductDetails(@RequestParam String asin, @RequestParam(defaultValue = "US") String country)
    {
        // Pass the request to the Service and return the result to the client
        return amazonScraperService.fetchProductRawData(asin, country);
    }
}