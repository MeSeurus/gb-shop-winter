package ru.gb.api.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.gb.api.category.api.CategoryGateway;
import ru.gb.api.manufacturer.api.ManufacturerGateway;
import ru.gb.api.product.api.ProductGateway;

@Configuration
@EnableConfigurationProperties(GbApiProperties.class)
@RequiredArgsConstructor
@Import(value = {FeignClientFactory.class})
public class FeignConfiguration {

    private final GbApiProperties gbApiProperties;

    private final FeignClientFactory feignClientFactory;

    @Bean
    public ManufacturerGateway manufacturerGateway() {
        return feignClientFactory.newFeignClient(ManufacturerGateway.class, gbApiProperties.getEndpoint().getManufacturerUrl());
    }

    @Bean
    public CategoryGateway categoryGateway() {
        return feignClientFactory.newFeignClient(CategoryGateway.class, gbApiProperties.getEndpoint().getCategoryUrl());
    }

    @Bean
    public ProductGateway productGateway() {
        return feignClientFactory.newFeignClient(ProductGateway.class, gbApiProperties.getEndpoint().getProductUrl());
    }
}
