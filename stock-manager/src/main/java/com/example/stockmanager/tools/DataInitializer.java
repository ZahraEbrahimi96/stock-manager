package com.example.stockmanager.tools;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {
    private final FakeDataService fakeDataService;
    private final ProductService productService;

    public DataInitializer(FakeDataService fakeDataService, ProductService productService) {
        this.fakeDataService = fakeDataService;
        this.productService = productService;
    }


    @Override
    public void run(String... args) throws Exception {
        int n = 100;
        for (int i = 0; i < n; i++) {
            Product product= fakeDataService.createFakeProduct();
            productService.save(product);
        }
        log.info("Product save success." + n);
    }
}