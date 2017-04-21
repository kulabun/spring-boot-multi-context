package com.labunco.configuration;

import com.labunco.auth.FirstUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@Order(1)
public class FirstSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic();

        UserDetailsService userDetailsService = new FirstUserDetailsService();
        getApplicationContext().getAutowireCapableBeanFactory().autowireBean(userDetailsService);
        http.antMatcher("/first/**").userDetailsService(userDetailsService);
    }
}
