package team5.onlybuns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team5.onlybuns.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

