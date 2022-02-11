package ru.gb.api.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import ru.gb.api.category.api.CategoryGateway;
import ru.gb.api.manufacturer.api.ManufacturerGateway;
import ru.gb.api.product.api.ProductGateway;

@Configuration
@EnableFeignClients(clients = {CategoryGateway.class, ManufacturerGateway.class, ProductGateway.class})
public class FeignConfiguration {
}
