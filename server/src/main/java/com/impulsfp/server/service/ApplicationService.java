package com.impulsfp.server.service;

import com.impulsfp.server.dto.ApplicationDto;
import com.impulsfp.server.dto.ApplyDto;
import com.impulsfp.server.enums.ApplicationStatus;
import com.impulsfp.server.enums.TestResult;
import com.impulsfp.server.exception.*;
import com.impulsfp.server.mapper.ApplicationMapper;
import com.impulsfp.server.model.*;
import com.impulsfp.server.repository.*;
import com.impulsfp.server.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Servei que gestiona les aplicacions a les ofertes; Permet als estudiants aplicar a les ofertes, veure les seves aplicacions i permet a les empreses gestionar els candidats.
 *
 * @author Jonathan Giraldo Giraldo
 */
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final OfferRepository offerRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationMapper applicationMapper;
    private final OfferTestRepository offerTestRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository,
                              StudentRepository studentRepository,
                              OfferRepository offerRepository,
                              CompanyRepository companyRepository,
                              ApplicationMapper applicationMapper, OfferTestRepository offerTestRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.offerRepository = offerRepository;
        this.companyRepository = companyRepository;
        this.applicationMapper = applicationMapper;
        this.offerTestRepository = offerTestRepository;
    }

    /**
     * Permet a un estudiant aplicar a una oferta; Verifica la sessió, comprova que l'usuari és un estudiant, verifica que l'oferta existeix i que no ha aplicat abans, i crea una nova aplicació amb estat PENDING.
     * @param sessionId La sessió de l'usuari que vol aplicar a l'oferta; S'utilitza per verificar la identitat i el rol de l'usuari.
     * @param dto Un objecte ApplyDto que conté l'identificador de l'oferta a la qual es vol aplicar i la resposta a un possible test associat a l'oferta
     */
    @Transactional
    public void apply(String sessionId, ApplyDto dto){

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

        Offer offer = offerRepository.findById(dto.getOfferId())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        if(applicationRepository.existsByStudentAndOffer(student, offer)){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Ja aplicat");
        }

        Application app = new Application();
        app.setStudent(student);
        app.setOffer(offer);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        OfferTest test = offerTestRepository.findByOffer(offer).orElse(null);

        if(test != null && (dto.getAnswer() == null || dto.getAnswer().isBlank())){
            throw new ApiException(ErrorCode.INVALID_REQUEST, "Cal respondre el test");
        }

        if(test != null){
            boolean passed = test.getCorrectAnswer()
                    .equalsIgnoreCase(dto.getAnswer().trim());

            app.setTestResult(passed ? TestResult.PASSED : TestResult.FAILED);
        }

        applicationRepository.save(app);
    }

    /**
     * Permet a un estudiant veure les seves aplicacions; Verifica la sessió, comprova que l'usuari és un estudiant, i retorna una llista de les aplicacions associades a aquest estudiant.
     * @param sessionId La sessió de l'usuari que vol veure les seves aplicacions; S'utilitza per verificar la identitat i el rol de l'usuari, i per obtenir l'estudiant associat a aquest usuari.
     * @return Una llista de ApplicationDto que representen les aplicacions associades a l'estudiant
     */
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

    /**
     * Permet a una empresa actualitzar l'estat d'una aplicació
     * @param sessionId La sessió de l'usuari que vol actualitzar l'estat de l'aplicació
     * @param applicationId L'identificador de l'aplicació que es vol actualitzar
     * @param status El nou estat de l'aplicació; S'espera que sigui un valor vàlid de l'enum ApplicationStatus (PENDING, ACCEPTED, REJECTED); S'utilitza per actualitzar l'estat de l'aplicació a aquest nou valor.
     */
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



    /**
     * Permet a una empresa veure els candidats que han aplicat a una oferta
     * @param sessionId La sessió de l'usuari que vol veure els candidats; S'utilitza per verificar la identitat i el rol de l'usuari, i per obtenir l'empresa associada a aquest usuari.
     * @param offerId L'identificador de l'oferta per la qual es vol veure els candidats
     * @return Una llista de ApplicationDto que representen les aplicacions associades a l'oferta
     */
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