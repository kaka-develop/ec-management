package org.group2.webapp.config;

import org.group2.webapp.security.AuthoritiesConstants;
import org.group2.webapp.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .accessDeniedPage("/error/error")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .deleteCookies("remember-me")
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .authorizeRequests()
                .antMatchers("/student/**").hasAnyAuthority(AuthoritiesConstants.STUDENT)
                .antMatchers("/coordinator/**").hasAnyAuthority(AuthoritiesConstants.COORDINATOR)
                .antMatchers("/manager/**").hasAnyAuthority(AuthoritiesConstants.MANAGER)
                .antMatchers("/admin/**").hasAnyAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/student/**").hasAnyAuthority(AuthoritiesConstants.STUDENT)
                .antMatchers("/api/coordinator/**").hasAnyAuthority(AuthoritiesConstants.COORDINATOR)
                .antMatchers("/api/manager/**").hasAnyAuthority(AuthoritiesConstants.MANAGER)
                .antMatchers("/api/admin/**").hasAnyAuthority(AuthoritiesConstants.ADMIN);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
