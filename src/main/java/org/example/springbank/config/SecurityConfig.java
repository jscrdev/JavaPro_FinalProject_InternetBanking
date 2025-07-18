package org.example.springbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfig(AuthHandler authHandler) {
        this.authenticationSuccessHandler = authHandler;
        System.out.println("Authentication Success Handler: " + authenticationSuccessHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests(
                        auth ->
                                auth.antMatchers("/")
                                        .hasAnyRole("USER", "ADMIN")
                                        .antMatchers(
                                                "/register",
                                                "/login",
                                                "/js/**",
                                                "/css/**",
                                                "/images/**",
                                                "/favicon.ico",
                                                "/logout")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .exceptionHandling(ex -> ex.accessDeniedPage("/error/403"))
                .formLogin(
                        form ->
                                form.loginPage("/login")
                                        .loginProcessingUrl("/j_spring_security_check")
                                        .failureUrl("/login?error")
                                        .usernameParameter("j_login")
                                        .passwordParameter("j_password")
                                        .permitAll())
                .oauth2Login(
                        oauth ->
                                oauth.loginPage("/login")
                                        .successHandler(authenticationSuccessHandler)
                                        .failureHandler(
                                                new SimpleUrlAuthenticationFailureHandler(
                                                        "/login?error")))
                .logout(
                        logout ->
                                logout.logoutUrl("/logout")
                                        .logoutSuccessUrl("/login?logout")
                                        .permitAll());
        return http.build();
    }
}
