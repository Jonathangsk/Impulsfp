package com.impulsfp.server.service;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
import com.impulsfp.server.model.Company;
import com.impulsfp.server.model.Student;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.CompanyRepository;
import com.impulsfp.server.repository.StudentRepository;
import com.impulsfp.server.repository.UserRepository;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.impulsfp.server.mapper.ProfileMapper;


/**
 * Servei per gestionar les operacions relacionades amb els usuaris, com la eliminació de comptes, modificació de dades personals, etc.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ProfileMapper profileMapper;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       CompanyRepository companyRepository,
                       ProfileMapper profileMapper) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.profileMapper = profileMapper;
    }

    /**
     * Elimina el compte de l'usuari associat a la sessió actual. Verifica que la sessió és vàlida i la contrassenya abans de procedir amb l'eliminació.
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar l'usuari que vol eliminar el seu compte, proporcionat com a paràmetre de la petició
     * @param password contrasenya de l'usuari, proporcionada al cos de la petició; s'espera que el JSON tingui un camp "password" amb la contrasenya de l'usuari
     */
    @Transactional
    public void deleteAccount(String sessionId, String password){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        //verifica que la contrassenya proporcionada coincideix amb la contrassenya emmagatzemada a la base de dades
        if(password == null || !passwordEncoder.matches(password, user.getPassword())){
            throw new ApiException(ErrorCode.INVALID_PASSWORD, "La contrasenya no és correcta");
        }

        userRepository.delete(user);

        SessionManager.removeSession(sessionId);
    }


    /**
     * Obté el perfil de l'usuari associat a la sessió actual. Verifica que la sessió és vàlida abans de procedir amb l'obtenció del perfil.
     * @param sessionId
     * @return
     */
    public Object getMyProfile(String sessionId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(user.getRole().equals("STUDENT")){
            Student student = studentRepository.findByUser(user)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Estudiant no trobat"));

            return profileMapper.toStudentDto(student);
        }

        if(user.getRole().equals("COMPANY")){
            Company company = companyRepository.findByUser(user)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

            return profileMapper.toCompanyDto(company);
        }

        throw new ApiException(ErrorCode.USER_NOT_FOUND, "Tipus d'usuari no vàlid");
    }





}