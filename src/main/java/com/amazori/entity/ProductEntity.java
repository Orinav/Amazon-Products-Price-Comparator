package com.amazori.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// @Entity tells Spring/Hibernate: "This class represents a table in the database!"
@Entity
// @Table allows us to name the table (if we don't, it will just be called 'product_entity')
@Table(name = "products")
// Lombok annotations to automatically generate Getters and Setters behind the scenes
@Getter
@Setter
public class ProductEntity
{
    // @Id tells Hibernate that this field is the Primary Key
    @Id
    private String asin;

    private String price;
    private String title;

    // We can save the country to know which regional Amazon site we scraped this from
    private String country;

    // A default empty constructor is required by Hibernate
    public ProductEntity()
    {
    }

    // A constructor for our convenience when creating new products
    public ProductEntity(String asin, String price, String title, String country) {
        this.asin = asin;
        this.price = price;
        this.title = title;
        this.country = country;
    }
}