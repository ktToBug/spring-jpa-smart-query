package io.github.kttobug.example.repository;

import io.github.kttobug.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 标准的 JPA Repository
    // 智能查询功能通过 SmartQueryService 提供
}