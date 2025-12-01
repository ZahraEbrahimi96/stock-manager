package com.example.stockmanager.tools;

import com.example.stockmanager.model.entity.Product;
import com.github.javafaker.Faker;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FakeDataService {
    @Resource
    private Faker faker = new Faker();

    public Product createFakeProduct(){
        Product product = new Product();
        product.setName(faker.name().name());
        product.setPrice(faker.number().numberBetween(50,100));
        product.setQuantity(faker.number().numberBetween(1,100));
        return product;
    }
}
