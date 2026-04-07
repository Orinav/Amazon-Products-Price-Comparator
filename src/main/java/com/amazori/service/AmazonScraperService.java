package com.amazori.service;

import com.amazori.dto.AmazonApiResponse;
import com.amazori.dto.ProductPriceResponse;
import com.amazori.entity.ProductEntity;
import com.amazori.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.net.URI;

@Service
public class AmazonScraperService
{
    private final RestClient restClient;

    @Value("${amazon.api.key}") //Get the API Key from application.properties.
    private String apiKey;

    @Value("${amazon.api.host}") //Get the API host from application.properties.
    private String apiHost;

    private final ProductRepository productRepository;

    //Constructor of the HTTP requests from the apiHost
    public AmazonScraperService(ProductRepository productRepository)
    {
        this.restClient = RestClient.builder().baseUrl("https://real-time-amazon-data.p.rapidapi.com").build();
        this.productRepository = productRepository;
    }

    public AmazonApiResponse fetchProductRawData(String asin, String country)
    {
        URI targetUri = UriComponentsBuilder //Initialize a builder to safely construct the URL (handles encoding and syntax automatically).
                .fromPath("/product-details")
                .queryParam("asin", asin)
                .queryParam("country", country)
                .build()
                .toUri(); //Finalize and convert the built components into a standard Java URI object.

        AmazonApiResponse parsedData = restClient.get()
                .uri(targetUri)
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .retrieve()
                .body(AmazonApiResponse.class); //Extract the JSON response body and save it as AmazonApiResponse format.

        return parsedData;
    }

    public AmazonApiResponse getProductDetailsSmartly(String asin, String country)
    {
        // Step A: Check our local database (warehouse) first
        Optional<ProductEntity> existingProduct = productRepository.findById(asin);

        if (existingProduct.isPresent())
        {
            // Scenario 1: Product found locally! We saved an API request!
            System.out.println("✅ Hit! Found product " + asin + " in our Local Database.");

            // Extract the entity from the wrapper and map it back to the DTO format the client expects
            ProductEntity entity = existingProduct.get();
            ProductPriceResponse data = new ProductPriceResponse(entity.getAsin(), entity.getPrice(), null, null, null);
            return new AmazonApiResponse("OK", data);
        }
        else
        {
            // Scenario 2: Product not found locally. We must fetch it from Amazon (RapidAPI)...
            System.out.println("❌ Miss! Fetching " + asin + " from Amazon (RapidAPI)...");

            // 1. Call our existing method that fetches data from the internet
            AmazonApiResponse responseFromAmazon = fetchProductRawData(asin, country);

            // 2. Ensure the response is valid before saving
            if (responseFromAmazon != null && responseFromAmazon.data() != null)
            {
                // 3. Create a new database entity from the fetched data
                ProductEntity newProduct = new ProductEntity(
                        responseFromAmazon.data().asin(),
                        responseFromAmazon.data().price(),
                        "Title will be added later", // Placeholder: We don't have Title in the DTO yet
                        country
                );

                // 4. Tell the repository to save this new product for future use
                productRepository.save(newProduct);
                System.out.println("💾 Saved " + asin + " to Database for future use.");
            }

            // Return the fetched response back to the client
            return responseFromAmazon;
        }
    }
}