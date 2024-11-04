package com.sport.sportinfractructureapi.repository;

import com.sport.sportinfractructureapi.model.Key;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
        boolean existsByUserLogin(String userLogin);
        boolean existsByUserLoginAndUserPassword(String login, String password);
        List<Key> findByUserLogin(String userLogin);
        Optional<Key> findByUserLoginAndUserPassword(String userLogin, String userPassword);
}