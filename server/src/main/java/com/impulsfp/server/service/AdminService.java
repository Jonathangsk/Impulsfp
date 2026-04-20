package com.impulsfp.server.service;

import com.impulsfp.server.dto.CompanyProfileDto;
import com.impulsfp.server.dto.OfferResponseDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
import com.impulsfp.server.mapper.OfferMapper;
import com.impulsfp.server.mapper.ProfileMapper;
import com.impulsfp.server.model.Company;
import com.impulsfp.server.model.Offer;
import com.impulsfp.server.model.Student;
import com.impulsfp.server.model.User;
import com.impulsfp.server.repository.CompanyRepository;
import com.impulsfp.server.repository.OfferRepository;
import com.impulsfp.server.repository.StudentRepository;
import com.impulsfp.server.repository.UserRepository;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Servei que proporciona funcionalitats d'administració, com obtenir llistats d'estudiants i empreses, i eliminar estudiants, empreses i ofertes.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Service
public class AdminService {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final OfferMapper offerMapper;

    public AdminService(StudentRepository studentRepository,
                        CompanyRepository companyRepository,
                        OfferRepository offerRepository,
                        UserRepository userRepository,
                        ProfileMapper profileMapper, OfferMapper offerMapper) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
        this.offerMapper = offerMapper;
    }

    /**
     * Obté una llista de tots els estudiants registrats a la plataforma. Requereix que l'usuari sigui administrador.
     * @param sessionId El ID de sessió de l'usuari que sol·licita la informació
     * @return Una llista de StudentProfileDto que representa els estudiants registrats a la plataforma
     */
    public List<StudentProfileDto> getAllStudents(String sessionId){

        SessionManager.requireAdmin(sessionId, userRepository);

        return studentRepository.findAll()
                .stream()
                .map(profileMapper::toStudentDto)
                .collect(Collectors.toList());
    }


    /**
     * Obté una llista de totes les empreses registrades a la plataforma. Requereix que l'usuari sigui administrador.
     * @param sessionId El ID de sessió de l'usuari que sol·licita la informació
     * @return Una llista de CompanyProfileDto que representa les empreses registrades a la plataforma
     */
    public List<CompanyProfileDto> getAllCompanies(String sessionId){

        SessionManager.requireAdmin(sessionId, userRepository);

        return companyRepository.findAll()
                .stream()
                .map(profileMapper::toCompanyDto)
                .collect(Collectors.toList());
    }


    /**
     * Elimina un estudiant de la plataforma, juntament amb l'usuari associat a aquest estudiant. Requereix que l'usuari sigui administrador.
     * @param sessionId El ID de sessió de l'usuari que sol·licita l'eliminació
     * @param id El ID de l'estudiant que es vol eliminar
     */
    public void deleteStudent(String sessionId, Long id){

        SessionManager.requireAdmin(sessionId, userRepository);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Estudiant no trobat"));

        User user = student.getUser();
        userRepository.delete(user);    }


    /**
     * Elimina una empresa de la plataforma, juntament amb l'usuari associat a aquesta empresa. Requereix que l'usuari sigui administrador.
     * @param sessionId El ID de sessió de l'usuari que sol·licita l'eliminació
     * @param id El ID de l'empresa que es vol eliminar
     */
    public void deleteCompany(String sessionId, Long id){

        SessionManager.requireAdmin(sessionId, userRepository);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        User user = company.getUser();
        userRepository.delete(user);
    }


    /**
     * Elimina una oferta de la plataforma. Requereix que l'usuari sigui administrador.
     * @param sessionId El ID de sessió de l'usuari que sol·licita l'eliminació
     * @param id El ID de l'oferta que es vol eliminar
     */
    public void deleteOffer(String sessionId, Long id){

        SessionManager.requireAdmin(sessionId, userRepository);

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        offerRepository.delete(offer);
    }

    /**
     * Obté una llista de totes les ofertes registrades a la plataforma. Requereix que l'usuari sigui administrador.
     * @param sessionId El ID de sessió de l'usuari que sol·licita la informació
     * @return Una llista de OfferResponseDto que representa les ofertes registrades a la plataforma
     */
    public List<OfferResponseDto> getAllOffers(String sessionId){

        SessionManager.requireAdmin(sessionId, userRepository);

        return offerRepository.findAll()
                .stream()
                .map(offerMapper::toDto)
                .collect(Collectors.toList());
    }
}