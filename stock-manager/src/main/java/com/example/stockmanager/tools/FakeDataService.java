package com.example.stockmanager.tools;

import com.example.stockmanager.model.entity.Product;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

@Service
public class FakeDataService {
    private final Faker faker = new Faker();

    public Product createFakeProduct(){
        return Product
                .builder()
                .name(faker.book().title())
                .price(faker.number().numberBetween(50, 5000))
                .quantity(faker.number().numberBetween(1, 10))
                .build();

    }
}
