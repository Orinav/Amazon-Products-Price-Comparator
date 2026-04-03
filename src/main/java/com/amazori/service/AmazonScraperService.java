package com.amazori.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class AmazonScraperService
{
    private final RestClient restClient;

    @Value("${amazon.api.key}") //Get the API Key from application.properties.
    private String apiKey;

    @Value("${amazon.api.host}") //Get the API host from application.properties.
    private String apiHost;

    //Constructor of the HTTP requests from the apiHost
    public AmazonScraperService(RestClient.Builder restClientBuilder)
    {
        this.restClient = restClientBuilder.baseUrl("https://real-time-amazon-data.p.rapidapi.com").build();
    }

    public String fetchProductRawData(String asin, String country)
    {
        URI targetUri = UriComponentsBuilder //Initialize a builder to safely construct the URL (handles encoding and syntax automatically).
                .fromPath("/product-details")
                .queryParam("asin", asin)
                .queryParam("country", country)
                .build()
                .toUri(); //Finalize and convert the built components into a standard Java URI object.

        String jsonData = restClient.get()
                .uri(targetUri)
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .retrieve()
                .body(String.class); //Extract the JSON response body and save it as raw text (String).

        return jsonData;
    }
}