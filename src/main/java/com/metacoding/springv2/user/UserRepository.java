package com.metacoding.springv2.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

// findById, findAll, save, deleteById, count를 가지고 있다 = crud
public interface UserRepository extends JpaRepository<User, Integer> { // crud를 안만들어도 된다.

    @Query("select u from User u where u.username = :username")
    public Optional<User> findByUsername(@Param("username") String username);
}