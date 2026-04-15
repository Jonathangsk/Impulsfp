package com.impulsfp.server.service;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
import com.impulsfp.server.model.*;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.impulsfp.server.mapper.ProfileMapper;

import java.util.List;
import java.util.Map;


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
    private final StudentSkillRepository studentSkillRepository;
    private final CompanyTechnologyRepository companyTechnologyRepository;


    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       CompanyRepository companyRepository,
                       StudentSkillRepository studentSkillRepository,
                       CompanyTechnologyRepository companyTechnologyRepository,
                       ProfileMapper profileMapper) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.companyTechnologyRepository = companyTechnologyRepository;
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
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar l'usuari del qual es vol obtenir el perfil, proporcionado como parámetro de la petición
     * @return un objecte que representa les dades del perfil de l'usuari
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


    /**
     * Actualitza el perfil de l'usuari associat a la sessió actual.
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar l'usuari del qual es vol actualitzar el perfil, proporcionat com a paràmetre de la petició
     * @param body objecte JSON que conté les dades del perfil que es vol actualitzar, proporcionat al cos de la petició; el format del JSON dependrà de les dades que es vulguin actualitzar, però pot incloure camps com "name", "email", "phone", etc.
     */
    @Transactional
    public void updateProfile(String sessionId, Map<String, Object> body){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(user.getRole().equals("STUDENT")){
            updateStudent(user, body);
            return;
        }

        if(user.getRole().equals("COMPANY")){
            updateCompany(user, body);
            return;
        }

        throw new ApiException(ErrorCode.USER_NOT_FOUND, "Tipus d'usuari no vàlid");
    }


    /**
     * Actualitza el perfil de l'estudiant associat a l'usuari
     * @param user objecte User que representa l'usuari del qual es vol actualitzar el perfil d'estudiant
     * @param body objecte JSON que conté les dades del perfil d'estudiant que es vol actualitzar, proporcionat al cos de la petició; el format del JSON dependrà de les dades que es vulguin actualitzar, però pot incloure camps com "city", "bio", "preferredLocation", "availability", "portfolio", "experienceLevel", "languages", "preferredRoles" i "skills"
     */
    private void updateStudent(User user, Map<String, Object> body){

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Estudiant no trobat"));

        //si el JSON conté un camp "city", actualitza el camp city de l'estudiant; si no, deixa el valor actual; el mateix s'aplica a la resta
        if(body.containsKey("city"))
            student.setCity((String) body.get("city"));

        if(body.containsKey("bio"))
            student.setBio((String) body.get("bio"));

        if(body.containsKey("preferredLocation"))
            student.setPreferredLocation((String) body.get("preferredLocation"));

        if(body.containsKey("availability"))
            student.setAvailability((String) body.get("availability"));

        if(body.containsKey("portfolio"))
            student.setPortfolio((String) body.get("portfolio"));

        if(body.containsKey("experienceLevel"))
            student.setExperienceLevel((String) body.get("experienceLevel"));

        if(body.containsKey("username")) //no es pot modificar el nom d'usuari d'un estudiant, ja que està associat a l'usuari i no a l'estudiant; si el JSON conté un camp "username", retorna un error
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No es pot modificar el nom d'usuari d'un estudiant");

        if(body.containsKey("surname") && body.get("surname") != null)
            student.setSurname((String) body.get("surname"));

        if(body.containsKey("name") && body.get("name") != null)
            student.setName((String) body.get("name"));

        if(body.containsKey("email") && body.get("email") != null)
            student.setEmail((String) body.get("email"));

        if(body.containsKey("cycle") && body.get("cycle") != null)
            student.setCycle((String) body.get("cycle"));

        if(body.containsKey("phoneNumber"))
            student.setPhoneNumber((String) body.get("phoneNumber"));








        //llistes de llenguatges i rols preferits, que es guarden com a strings separats per comes a la base de dades;
        // si el JSON conté un camp "languages" o "preferredRoles", actualitza els camps corresponents de l'estudiant; si no, deixa els valors actuals
        if(body.containsKey("languages")){
            List<?> raw = (List<?>) body.get("languages"); //raw és una llista d'objectes, ja que el JSON pot contenir qualsevol tipus de dades; per això, es fa un cast a List<?> per evitar errors de tipus
            List<String> langs = raw.stream().map(Object::toString).toList(); //es converteix cada element de la llista a string, ja que el camp languages de l'estudiant és un string separat per comes; després, es fa una llista de strings amb els llenguatges preferits

            student.setLanguages(String.join(",", langs));
        }

        if(body.containsKey("preferredRoles")){
            List<?> raw = (List<?>) body.get("preferredRoles");
            List<String> roles = raw.stream().map(Object::toString).toList();

            student.setPreferredRoles(String.join(",", roles));
        }

        //skills
        if(body.containsKey("skills")){
            List<?> raw = (List<?>) body.get("skills");
            List<String> skills = raw.stream().map(Object::toString).toList();

            studentSkillRepository.deleteByStudent(student);

            for(String skill : skills){
                StudentSkill s = new StudentSkill();
                s.setStudent(student);
                s.setSkill(skill);
                studentSkillRepository.save(s);
            }
        }

        studentRepository.save(student);
    }



    /**
     * Actualitza el perfil de l'empresa associada a l'usuari. Verifica que l'usuari existeix i que és una empresa abans de procedir amb l'actualització del perfil.
     * @param user objecte User que representa l'usuari del qual es vol actualitzar el perfil d'empresa
     * @param body objecte JSON que conté les dades del perfil d'empresa que es vol actualitzar, proporcionat al cos de la petició; el format del JSON dependrà de les dades que es vulguin actualitzar, però pot incloure camps com "name", "address", "phone", "website", "niche" i "technologies"
     */
    private void updateCompany(User user, Map<String, Object> body){

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        if(body.containsKey("name"))
            company.setName((String) body.get("name"));

        if(body.containsKey("address"))
            company.setAddress((String) body.get("address"));

        if(body.containsKey("phone"))
            company.setPhone((String) body.get("phone"));

        if(body.containsKey("website"))
            company.setWebsite((String) body.get("website"));

        if(body.containsKey("niche"))
            company.setNiche((String) body.get("niche"));

        if(body.containsKey("activeOffers"))
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No es pot modificar el nombre d'ofertes actives d'una empresa");

        if(body.containsKey("username")) //no es pot modificar el nom d'usuari d'una empresa, ja que està associat a l'usuari i no a l'empresa; si el JSON conté un camp "username", retorna un error
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No es pot modificar el nom d'usuari d'una empresa");

        //technologies
        if(body.containsKey("technologies")){
            List<?> raw = (List<?>) body.get("technologies"); //raw és una llista d'objectes, ja que el JSON pot contenir qualsevol tipus de dades; per això, es fa un cast a List<?> per evitar errors de tipus
            List<String> techs = raw.stream().map(Object::toString).toList(); //es converteix cada element de la llista a string, ja que el camp technologies de l'empresa és un string separat per comes; després, es fa una llista de strings amb les tecnologies

            companyTechnologyRepository.deleteAll(company.getTechnologies());

            for(String tech : techs){
                CompanyTechnology t = new CompanyTechnology();
                t.setCompany(company);
                t.setTechnology(tech);
                companyTechnologyRepository.save(t);
            }
        }

        companyRepository.save(company);
    }



}