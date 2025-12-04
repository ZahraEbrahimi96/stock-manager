package com.example.stockmanager.tools;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.service.ProductService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Resource
    private FakeDataService fakeDataService;
    @Resource
    private ProductService productService;


    @Override
    public void run(String... args) throws Exception {
        int n = 100;
        for (int i = 0; i < n; i++) {
            final Product product= fakeDataService.createFakeProduct();
            productService.save(product);
        }
        log.info("Product save success." + n);
    }
}