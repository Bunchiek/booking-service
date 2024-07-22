package com.example.booking.service.impl;

import com.example.booking.entity.RoleType;
import com.example.booking.entity.User;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.UserService;
import com.example.booking.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(()->
                new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден!",id)));
    }

    @Override
    public User save(User user, RoleType roleType) {
        user.setRole(roleType);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        User existedCategory = findById(user.getId());

        BeanUtils.copyNonNullProperties(user, existedCategory);

        return repository.save(existedCategory);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow();
        return repository.findByUsername(username).orElseThrow(()->
                new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден!",username)));
    }
}
