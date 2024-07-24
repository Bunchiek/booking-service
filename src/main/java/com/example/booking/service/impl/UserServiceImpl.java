package com.example.booking.service.impl;

import com.example.booking.entity.RoleType;
import com.example.booking.entity.User;
import com.example.booking.events.RegistrationEvent;
import com.example.booking.exception.EntityNotFoundException;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.UserService;
import com.example.booking.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.kafka.kafkaRegisterTopic}")
    private String kafkaRegisterTopic;

    private final KafkaTemplate<String, RegistrationEvent> kafkaRegistrationEventProducerFactory;

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
        User savedUser = repository.save(user);

        RegistrationEvent event = new RegistrationEvent();
        event.setUserId(savedUser.getId());
        kafkaRegistrationEventProducerFactory.send(kafkaRegisterTopic, event);

        return savedUser;
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
        return repository.findByUsername(username).orElseThrow(()->
                new EntityNotFoundException(MessageFormat.format("Пользователь с именем {0} не найден!",username)));
    }
}
