package ru.gb.api.configuration;

import feign.*;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Component;

import static feign.FeignException.errorStatus;

@Component
@RequiredArgsConstructor
public class FeignClientFactory {

    private final GbApiProperties gbApiProperties;
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public <T> T newFeignClient(Class<T> requiredType, String url) {

        return Feign.builder()
                .encoder(new SpringEncoder(this.messageConverters))
                .decoder(new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters))))
                .options(new Request.Options(
                        gbApiProperties.getConnection().getConnectTimeoutMillis(),
                        gbApiProperties.getConnection().getReadTimeoutMillis()
                ))
                .logger(new Slf4jLogger(requiredType))
                .logLevel(Logger.Level.FULL)
                .retryer(new Retryer.Default(
                        gbApiProperties.getConnection().getPeriod(),
                        gbApiProperties.getConnection().getMaxPeriod(),
                        gbApiProperties.getConnection().getMaxAttempts()
                ))
                .errorDecoder(errorDecoder())
                .contract(new SpringMvcContract())
                .target(requiredType, url);
    }

    private ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            FeignException feignException = errorStatus(methodKey, response);
            if (feignException.status() == 500 || feignException.status() == 503) {
                return new RetryableException(
                        response.status(),
                        feignException.getMessage(),
                        response.request().httpMethod(),
                        feignException,
                        null,
                        response.request());
            }
            return feignException;
        };
    }
}