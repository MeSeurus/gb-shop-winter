package ru.gb.configserver.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration {

    @Configuration
    @Order(1)
    @ConditionalOnProperty(value = "security.endpoint.decrypt.enabled", matchIfMissing = true, havingValue = "false")
    public static class DenyDecryptEndpointAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/decrypt").denyAll();
        }
    }

    @Order(2)
    @Configuration
    public static class PermitAllAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests().anyRequest().permitAll();
        }
    }
}
