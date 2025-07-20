package com.connectify.demo.Service;
import com.connectify.demo.Exceptions.UserNotFoundException;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoService userInfoService;

    private UserInfo sampleUser;

    @BeforeEach
    void setUp() {

        sampleUser = new UserInfo();
        sampleUser.setId(1L);
        sampleUser.setUsername("shubham9344");
        sampleUser.setEmail("kumarshubham2004a@gmail.com");
        sampleUser.setName("shubham");
        sampleUser.setUserBio("software-developer");
        sampleUser.setPassword("plainPass");
    }

    @Test
    @DisplayName("addUser() should save and return the saved user")
    void whenAddUser_thenRepositorySaveIsCalled_andReturnsUser() {
        given(passwordEncoder.encode("plainPass"))
                .willReturn("encodedPass");

        // simulate JPA assigning an ID when saving
        given(userInfoRepository.save(any(UserInfo.class)))
                .willAnswer(invocation -> {
                    UserInfo toSave = invocation.getArgument(0);
                    toSave.setId(42L);
                    return toSave;
                });

        UserInfo saved = userInfoService.AddUser(sampleUser);

        verify(passwordEncoder).encode("plainPass");
        verify(userInfoRepository).save(sampleUser);

        assertThat(saved.getId()).isEqualTo(42L);
        assertThat(saved.getPassword()).isEqualTo("encodedPass");
        assertThat(saved.getEmail()).isEqualTo(sampleUser.getEmail());
    }

    @Test
    @DisplayName("getUserbyId() returns a user when present")
    void whenGetUserById_andUserExists_thenReturnUser() {

        given(userInfoRepository.findById(1L))
                .willReturn(Optional.of(sampleUser));

        UserInfo found = userInfoService.getUserbyId(1L);

        verify(userInfoRepository).findById(1L);
        assertThat(found.getEmail())
                .isEqualTo("kumarshubham2004a@gmail.com");
        assertThat(found.getUsername())
                .isEqualTo("shubham9344");
    }

    @Test
    @DisplayName("getUserbyId() throws when user not found")
    void whenGetUserById_andNotExists_thenThrow() {
        // stub findById to return empty
        given(userInfoRepository.findById(999L))
                .willReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userInfoService.getUserbyId(999L)
        );

        verify(userInfoRepository).findById(999L);
    }
}