package com.birdbi.api.configure;

import com.birdbi.api.security.AdminDetailsServiceImpl;
import com.birdbi.api.security.UserDetailsServiceImpl;
import com.birdbi.api.security.filter.UserAuthenticationFilter;
import com.birdbi.api.security.jwt.JwtAuthenticationFilter;
import com.birdbi.api.security.jwt.JwtTokenProvider;
import com.birdbi.api.security.provider.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            //Actuator
            "/actuator/**",

            "/**/webjars/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            // 유저 로그인
            "/birdbi/api/v1/user/login",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/birdbi/images/**",
    };

    public UserAuthenticationProvider userProvider() {
        return new UserAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        UserAuthenticationFilter userAuthFilter = new UserAuthenticationFilter(jwtTokenProvider, userProvider());
        userAuthFilter.setFilterProcessesUrl("/birdbi/api/v1/user/login");

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"
                ,"http://localhost:8080"
                ,"http://localhost"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization", "access-token", "refresh-token","enckey"));
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.OPTIONS, "/birdbi/api/v1/**").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                //.antMatchers("/birdbi/api/v1/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .antMatchers("/birdbi/api/v1/user/**").hasAnyRole("USER");

        http.addFilter(userAuthFilter);
        http.cors().configurationSource(request -> corsConfiguration);
        //http.addFilter(adminAuthFilter);
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/**/webjars/**",
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/v3/api-docs/**",
                //"/birdbi/api/v1//admin/**",
                "/birdbi/api/v1/user/**",
                //"http://localhost:8080/images/",
                "/birdbi/images/**",
                "/error");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}