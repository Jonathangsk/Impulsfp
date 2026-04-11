package com.impulsfp.server.service;

import com.impulsfp.server.dto.LoginResponseDto;
import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.UserRepository;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import com.impulsfp.server.dto.RegisterStudentRequestDto;
import com.impulsfp.server.dto.RegisterCompanyRequestDto;
import com.impulsfp.server.model.*;
import com.impulsfp.server.repository.*;

import java.util.Optional;

/**
 * Clase Service que implementa la lògica d'autenticació dels usuaris. Verifica les credencials i gestiona les sessions.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Service
public class AuthService {

    private final UserRepository userRepository; //repositori per accedir a les dades de l'usuari (base de dades)
    private final StudentRepository studentRepository; //repositori per accedir a les dades dels estudiants (base de dades)
    private final CompanyRepository companyRepository; //repositori per accedir a les dades de les empreses (base de dades)
    private final StudentSkillRepository studentSkillRepository; //repositori per accedir a les dades de les habilitats dels estudiants (base de dades)
    private final CompanyTechnologyRepository companyTechnologyRepository; //repositori per accedir a les dades de les tecnologies de les empreses (base de dades)


    /**
     * Constructor de la classe AuthService; s'injecten els repositoris necessaris per accedir a les dades dels usuaris, estudiants, empreses, habilitats i tecnologies
     * @param userRepository
     * @param studentRepository
     * @param companyRepository
     * @param studentSkillRepository
     * @param companyTechnologyRepository
     */
    public AuthService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       CompanyRepository companyRepository,
                       StudentSkillRepository studentSkillRepository,
                       CompanyTechnologyRepository companyTechnologyRepository) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.companyTechnologyRepository = companyTechnologyRepository;
    }


    /**
     * Mètode amb la lògica iniciar sessió; verifica les credencials de l'usuari i, si són correctes, crea una sessió i retorna un objecte DTO de resposta amb les dades de sessió (sessionId i userType)
     * Si les credencials no són correctes, retorna null.
     * @param username nom d'usuari
     * @param password contrasenya
     * @return
     */
    public LoginResponseDto login(String username, String password) {

        Optional<User> userOpt = userRepository.findByUsername(username);

        if(userOpt.isPresent()) {
            User user = userOpt.get();

            if(user.getPassword().equals(password)) {
                String sessionId = SessionManager.createSession(username);
                return new LoginResponseDto(sessionId, user.getRole());
            }
        }

        return null;
    }


    /**
     * Mètode amb la lògica per registrar un nou estudiant; verifica que el nom d'usuari no existeix, crea un nou usuari i un nou estudiant associat,
     * i retorna un objecte DTO de resposta amb les dades de sessió (sessionId i userType) si el registre és correcte.
     * Si el nom d'usuari ja existeix, retorna null.
     * @param dto
     * @return
     */
    public LoginResponseDto registerStudent(RegisterStudentRequestDto dto) {

        validarDadesStudiant(dto); //crida a mètode per validar les dades del DTO proporcionat

        //crea un nou usuari amb les dades proporcionades al DTO i el rol "STUDENT"
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole("STUDENT");

        //guarda el nou usuari a la base de dades
        userRepository.save(user);

        //crea un nou estudiant associat a l'usuari creat, amb les dades proporcionades al DTO
        Student student = new Student();
        student.setUser(user);
        student.setName(dto.getName());
        student.setSurname(dto.getSurname());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setCycle(dto.getCycle());


        //si el DTO conté llistes de llenguatges i rols preferits, les converteix a cadenes separades per comes i les guarda a l'estudiant
        if(dto.getLanguages() != null)
            student.setLanguages(String.join(",", dto.getLanguages()));

        //si el DTO conté una llista de rols preferits, la converteix a una cadena separada per comes i la guarda a l'estudiant
        if(dto.getPreferredRoles() != null)
            student.setPreferredRoles(String.join(",", dto.getPreferredRoles()));

        //guarda el nou estudiant a la base de dades
        studentRepository.save(student);

        //si el DTO conté una llista d'habilitats, crea un nou objecte StudentSkill per cada habilitat i els guarda a la base de dades associats a l'estudiant creat
        if(dto.getSkills() != null){
            for(String skill : dto.getSkills()){
                StudentSkill s = new StudentSkill();
                s.setStudent(student);
                s.setSkill(skill);
                studentSkillRepository.save(s);
            }
        }

        //crea una nova sessió per l'usuari creat i retorna un objecte DTO de resposta amb les dades de sessió (sessionId i userType)
        String sessionId = SessionManager.createSession(user.getUsername());

        return new LoginResponseDto(sessionId, user.getRole());
    }


    /**
     * Mètode amb la lògica per registrar una nova empresa; verifica que el nom d'usuari no existeix,
     * crea un nou usuari i una nova empresa associada,
     * @param dto objecte DTO que representa les dades de la petició de registre d'empresa
     * @return
     */
    public LoginResponseDto registerCompany(RegisterCompanyRequestDto dto) {

        validarDadesEmpresa(dto); //crida a mètode per validar les dades del DTO proporcionat

        //crea un nou usuari amb les dades proporcionades al DTO i el rol "COMPANY"
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole("COMPANY");
        userRepository.save(user);

        //crea una nova empresa associada a l'usuari creat, amb les dades proporcionades al DTO
        Company company = new Company();
        company.setUser(user);
        company.setName(dto.getName());
        company.setEmail(dto.getEmail());
        company.setAddress(dto.getAddress());
        company.setVatNumber(dto.getVatNumber());
        company.setPhone(dto.getPhone());
        companyRepository.save(company);

        //si el DTO conté una llista de tecnologies, crea un nou objecte CompanyTechnology per cada tecnologia i
        // els guarda a la base de dades associats a l'empresa creada
        if(dto.getTechnologies() != null){
            for(String tech : dto.getTechnologies()){
                CompanyTechnology t = new CompanyTechnology();
                t.setCompany(company);
                t.setTechnology(tech);
                companyTechnologyRepository.save(t);
            }
        }

        //crea una nova sessió per l'usuari creat i retorna un objecte DTO de resposta amb les dades de sessió (sessionId i userType)
        String sessionId = SessionManager.createSession(user.getUsername());

        return new LoginResponseDto(sessionId, user.getRole());
    }


    /**
     * Mètode per validar les dades proporcionades al DTO de registre d'estudiant; verifica que el nom d'usuari no existeix a la base de dades, que el nom d'usuari és vàlid segons un regex, i que la contrasenya és vàlida segons un regex.
     * Si alguna de les validacions falla, llança una excepció ApiException amb un codi d'error i un missatge descriptiu.
     * @param dto
     */
    private void validarDadesStudiant(RegisterStudentRequestDto dto) {

        if(dto.getUsername() == null || !dto.getUsername().matches("^[a-zA-Z0-9]{4,20}$")){ //el regex verifica que el nom d'usuari només conté lletres i números (sense espais), i tingui entre 4 y 20 caracteres
            throw new ApiException(ErrorCode.INVALID_USERNAME, "El nom d'usuari no és vàlid. Ha de tenir entre 4 I 20 caracteres; només lletres i números.");
        }

        if(userRepository.findByUsername(dto.getUsername()).isPresent()){
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS, "El nom d'usuari ja existeix. Si us plau, tria un altre nom d'usuari.");
        }

        if (!dto.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[a-z]).{6,}$")) { //el regex verifica que la contrasenya té al menys 6 caracteres, una mayúscula, una minúscula y un número
            throw new ApiException(ErrorCode.INVALID_PASSWORD, "La contrasenya no és vàlida; > 6 caracteres, mínim una majúscula, una minúscula i un número.");
        }
    }



    /**
     * Mètode per validar les dades proporcionades al DTO de registre d'empresa; verifica que el nom d'usuari no existeix a la base de dades,
     * que el nom d'usuari és vàlid segons un regex, i que la contrasenya és vàlida segons un regex.
     *

     * Nota: aquí només validem username/password/existència per mantenir consistència amb estudiant.
     * Pots afegir més validacions (NIF/CIF, email, etc.) si ho necessites.
     *
     * @param dto
     */
    private void validarDadesEmpresa(RegisterCompanyRequestDto dto) {

        if (dto.getUsername() == null || !dto.getUsername().matches("^[a-zA-Z0-9]{4,20}$")) { //el regex verifica que el nom d'usuari només conté lletres i números (sense espais), i tingui entre 4 y 20 caracteres
            throw new ApiException(ErrorCode.INVALID_USERNAME, "El nom d'usuari no és vàlid. Ha de tenir entre 4 I 20 caracteres; només lletres i números.");
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ApiException(ErrorCode.USER_ALREADY_EXISTS, "El nom d'usuari ja existeix. L'usuari ha de triar un altre.");
        }

        if (!dto.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[a-z]).{6,}$")) { //el regex verifica que la contrasenya té al menys 6 caracteres, una mayúscula, una minúscula y un número
            throw new ApiException(ErrorCode.INVALID_PASSWORD, "La contrasenya no és vàlida; > 6 caracteres, mínim una majúscula, una minúscula i un número.");
        }
    }




}