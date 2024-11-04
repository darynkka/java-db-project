package com.sport.sportinfractructureapi.service;

import com.sport.sportinfractructureapi.model.Key;
import com.sport.sportinfractructureapi.repository.KeyRepository;
import com.sport.sportinfractructureapi.views.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class KeyService {

    @Autowired
    private KeyRepository keyRepository;
    @Autowired
    private SessionManager sessionManager;

    public Key authenticate(String userLogin, String userPassword) {
        Optional<Key> user = keyRepository.findByUserLoginAndUserPassword(userLogin, userPassword);
        if (user.isPresent()) {
            sessionManager.setAuthenticatedUser(user.get());
            return user.get();
        }
        return null;
    }

    public List<Key> getAllKeys() {
        return keyRepository.findAll();
    }

    public Optional<Key> getKeyById(Long id) {
        return keyRepository.findById(id);
    }

    public boolean userExists(String userLogin) {
        return keyRepository.existsByUserLogin(userLogin);
    }

    public void saveKey(Key key) {
        keyRepository.save(key);
    }
    public void deleteKey(Long id) {
        keyRepository.deleteById(id);
    }

    public List<Key> getPasswordByLogin(String login) {
        return keyRepository.findByUserLogin(login);
    }

    public List<Key> findByUserLogin(String userLogin) {
        return keyRepository.findByUserLogin(userLogin);
    }

    public void logout() {
        sessionManager.clearAuthenticatedUser();
    }
    public Key getAuthenticatedUser() {
        return sessionManager.getAuthenticatedUser();
    }

    public String getUsername() {
        return sessionManager.getAuthenticatedUser().getUserLogin();
    }

}
