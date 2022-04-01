package ru.gb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(
                (requests) -> {
                    requests.antMatchers("/").permitAll();
                    requests.antMatchers("/product/all").permitAll();
                    requests.antMatchers("/auth/**").permitAll();
                    requests.antMatchers(HttpMethod.POST, "/product").hasRole("ADMIN");
                    requests.mvcMatchers(HttpMethod.GET, "/product/{productId}").permitAll();
                }
        );
        http.authorizeRequests((requests) -> {
            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated();
        });
        http.exceptionHandling().accessDeniedPage("/errors/access-denied");
        http.formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll();
        http.logout()
                .logoutSuccessUrl("/product/all")
                .permitAll();
        http.httpBasic();
    }

}