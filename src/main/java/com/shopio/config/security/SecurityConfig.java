package com.shopio.config.security;

import com.shopio.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    UserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("root")
                        .password("{noop}root")
                        .roles("USER")
                        .build()
        );
    }
}
