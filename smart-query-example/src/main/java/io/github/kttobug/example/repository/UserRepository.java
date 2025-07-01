package io.github.kttobug.example.repository;

import io.github.kttobug.example.entity.User;
import io.github.kttobug.query.LambdaQueryWrapper;
import io.github.kttobug.spring.LambdaQueryExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, LambdaQueryExecutor<User> {

    default List<User> findActiveAdmins() {
        return list(LambdaQueryWrapper.of(User.class)
                .eq(User::getStatus, 1)
                .eq(User::getUsername, "admin"));
    }
}