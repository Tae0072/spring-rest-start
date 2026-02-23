package com.metacoding.springv2._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.metacoding.springv2._core.filter.JwtAuthorizationFilter;
import com.metacoding.springv2._core.util.RespFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 필터 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // CORS 설정
        http.cors(cors -> cors.configurationSource(configurationSource()));

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                        (request, response, authException) -> RespFilter.fail(response, 401, "로그인 후 이용해주세요"))
                .accessDeniedHandler(
                        (request, response, accessDeniedException) -> RespFilter.fail(response, 403, "권한이 없습니다")));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 인증/권한 주소 커스터마이징
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/api/boards/**").permitAll()
                .requestMatchers("/api/**")// api만 적혀 있으면 모두 인증이 필요함.
                .authenticated()
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .anyRequest().permitAll());

        // 폼 로그인 비활성화 ( POST : x-www-form-urlencoded : username, password )
        http.formLogin(f -> f.disable());

        // 베이직 인증 활성화 시킴 (request 할때마다 username, password를 요구)
        http.httpBasic(b -> b.disable());

        // csrf 비활성화
        http.csrf(c -> c.disable());

        // 인증 필터를 변경
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}