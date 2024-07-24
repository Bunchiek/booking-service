package com.example.booking.service;

import com.example.booking.entity.RoleType;
import com.example.booking.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User save(User user, RoleType roleType);
    User update(User user);
    void deleteById(Long id);
    User findByUsername(String username);
}
