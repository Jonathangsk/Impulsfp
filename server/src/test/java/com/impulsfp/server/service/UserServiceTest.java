package com.impulsfp.server.service;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.mapper.ProfileMapper;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.impulsfp.server.model.Student;
import com.impulsfp.server.dto.StudentProfileDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de tests unitaris per al servei UserService
 */
public class UserServiceTest {


    private UserService userService;

    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private CompanyRepository companyRepository;
    private StudentSkillRepository studentSkillRepository;
    private CompanyTechnologyRepository companyTechnologyRepository;
    private ProfileMapper profileMapper;

    /**
     * Configura els mocks i el servei abans de cada test
     */
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        companyRepository = Mockito.mock(CompanyRepository.class);
        studentSkillRepository = Mockito.mock(StudentSkillRepository.class);
        companyTechnologyRepository = Mockito.mock(CompanyTechnologyRepository.class);
        profileMapper = Mockito.mock(ProfileMapper.class);

        userService = new UserService(
                userRepository,
                studentRepository,
                companyRepository,
                studentSkillRepository,
                companyTechnologyRepository,
                profileMapper
        );
    }

    /**
     * Test per verificar que getMyProfile llança una excepció quan el sessionId no és vàlid
     */
    @Test
    void getProfileInvalidSession() {
        assertThrows(ApiException.class, () -> {
            userService.getMyProfile("invalid");
        });
    }


    /**
     * Test per verificar que deleteAccount llança una excepció quan el sessionId no és vàlid
     */
    @Test
    void deleteAccountWrongPassword() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        user.setPassword("1234");

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        assertThrows(ApiException.class, () -> {
            userService.deleteAccount(sessionId, "wrong");
        });
    }

    /**
     * Test per verificar que getMyProfile retorna el perfil de l'estudiant correctament quan el sessionId és vàlid i l'usuari és un estudiant
     */
    @Test
    void getProfileOk() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        user.setRole("STUDENT");

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        Student student = new Student();
        student.setUser(user);

        Mockito.when(studentRepository.findByUser(user))
                .thenReturn(Optional.of(student));

        Mockito.when(profileMapper.toStudentDto(student))
                .thenReturn(new StudentProfileDto());

        assertNotNull(userService.getMyProfile(sessionId));
    }

    /**
     * Test per verificar que deleteAccount elimina l'usuari correctament quan el sessionId és vàlid i la contrasenya és correcta
     */
    @Test
    void deleteAccountOk() {
        String sessionId = SessionManager.createSession("user");

        User user = new User();
        user.setUsername("user");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("Password1"));

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(user));

        userService.deleteAccount(sessionId, "Password1");

        Mockito.verify(userRepository).delete(user);
    }

    /**
     * Test per verificar que deleteAccount llança una excepció quan el sessionId no és vàlid
     */
    @Test
    void deleteAccountInvalidSession() {
        assertThrows(ApiException.class, () -> {
            userService.deleteAccount("invalid", "1234");
        });
    }


}
