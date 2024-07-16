package com.epam.hibernate.service;

import com.epam.hibernate.entity.User;
import com.epam.hibernate.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
    @Mock
    private UserJpaRepository userJpaRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void testSaveAdmin() {
        userService.saveAdmin();
        verify(userJpaRepository, times(1)).save(any(User.class));
    }
}
