package com.impulsfp.server.service;

import com.impulsfp.server.dto.ApplicationDto;
import com.impulsfp.server.enums.ApplicationStatus;
import com.impulsfp.server.exception.*;
import com.impulsfp.server.mapper.ApplicationMapper;
import com.impulsfp.server.model.*;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final OfferRepository offerRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationMapper applicationMapper;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository,
                              StudentRepository studentRepository,
                              OfferRepository offerRepository,
                              CompanyRepository companyRepository,
                              ApplicationMapper applicationMapper) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.offerRepository = offerRepository;
        this.companyRepository = companyRepository;
        this.applicationMapper = applicationMapper;
    }

    //APPLY
    @Transactional
    public void apply(String sessionId, Long offerId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        if(!user.getRole().equals("STUDENT")){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Només estudiants");
        }

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Estudiant no trobat"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        if(applicationRepository.existsByStudentAndOffer(student, offer)){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Ja aplicat");
        }

        Application app = new Application();
        app.setStudent(student);
        app.setOffer(offer);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        applicationRepository.save(app);

    }

    //MIS APPLICATIONS
    public List<ApplicationDto> getMyApplications(String sessionId){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }

        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Estudiant no trobat"));

        return applicationRepository.findByStudent(student)
                .stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    // UPDATE STATUS (empresa)
    @Transactional
    public void updateStatus(String sessionId, Long applicationId, String status){

        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }


        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "No trobada"));

        if(!app.getOffer().getCompany().getId().equals(company.getId())){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No autoritzat");
        }

        app.setStatus(ApplicationStatus.valueOf(status));
    }

    // APPLICANTS
    public List<ApplicationDto> getApplicants(String sessionId, Long offerId){


        if(!SessionManager.isValid(sessionId)){
            throw new ApiException(ErrorCode.INVALID_SESSION, "Sessió no vàlida");
        }


        String username = SessionManager.getUsername(sessionId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Usuari no trobat"));

        Company company = companyRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        if(!offer.getCompany().getId().equals(company.getId())){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "No autoritzat");
        }

        return applicationRepository.findByOffer(offer)
                .stream()
                .map(applicationMapper::toDto)
                .toList();
    }
}