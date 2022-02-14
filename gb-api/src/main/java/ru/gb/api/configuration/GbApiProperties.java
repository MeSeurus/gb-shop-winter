package ru.gb.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "gb.api")
public class GbApiProperties {

    private Endpoint endpoint;

    private Connection connection;

    @Getter
    @Setter
    public static class Endpoint {
        private String manufacturerUrl;
    }

    @Setter
    @Getter
    public static class Connection {
        private int period;
        private int maxPeriod;
        private int maxAttempts;
        private int connectTimeoutMillis;
        private int readTimeoutMillis;
    }
}
