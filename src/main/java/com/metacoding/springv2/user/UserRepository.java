package com.metacoding.springv2.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 사용자 엔티티에 대한 DB 접근을 담당하는 리포지토리.
 * <p>
 * - {@link JpaRepository} 기본 메서드(findById, save 등)를 활용하고<br>
 * - 유저네임으로 조회하는 커스텀 쿼리를 추가로 제공한다.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    // username 은 unique 제약이 있으므로 Optional<User>로 단일 결과만 반환
    @Query("select u from User u where u.username = :username")
    public Optional<User> findByUsername(@Param("username") String username);
}    