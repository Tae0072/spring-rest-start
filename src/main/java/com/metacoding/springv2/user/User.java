package com.metacoding.springv2.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 애플리케이션의 사용자 정보를 나타내는 JPA 엔티티.
 * <p>
 * - DB의 {@code user_tb} 테이블과 매핑되며<br>
 * - Spring Security의 {@link UserDetails}를 구현해 인증 객체로도 사용된다.
 * </p>
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_tb")
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    // 유저네임은 중복 불가, 최대 20자까지 허용
    @Column(unique = true, length = 20, nullable = false)
    private String username;
    // Bcrypt 해시값 길이에 맞춰 60자로 설정
    @Column(length = 60, nullable = false)
    private String password;
    @Column(length = 30, nullable = false)
    private String email;
    private String roles; // 디폴트값은 USER

    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public User(Integer id, String username, String password, String email, String roles, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    // 프로필 수정 시 이메일/패스워드를 함께 변경할 수 있도록 하는 도메인 메서드
    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    // UserDetails 구현: 문자열 roles를 Spring Security 권한 객체로 변환
    public Collection<? extends GrantedAuthority> getAuthorities() { // Collection에 권한이 담김.
        Collection<GrantedAuthority> as = new ArrayList<>();
        String[] roleList = roles.split(","); // User -> admin, user
        for (String role : roleList) {
            as.add(() -> "ROLE_" + role);
        }
        return as; // cos -> user, admin
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}