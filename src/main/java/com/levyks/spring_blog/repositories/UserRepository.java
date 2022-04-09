package com.levyks.spring_blog.repositories;

import com.levyks.spring_blog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query(value = "select u from User u where lower(concat(u.firstName, ' ', u.lastName)) like concat('%', lower(?1), '%')")
    Page<User> searchByName(String name, Pageable pageable);
}
