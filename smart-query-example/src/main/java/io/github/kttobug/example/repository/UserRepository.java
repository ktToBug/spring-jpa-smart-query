package io.github.kttobug.example.repository;

import io.github.kttobug.example.entity.User;
import io.github.kttobug.spring.LambdaQueryExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, LambdaQueryExecutor<User> {

}