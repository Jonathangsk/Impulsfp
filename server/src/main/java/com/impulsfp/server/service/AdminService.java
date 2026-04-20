package com.impulsfp.server.service;

import com.impulsfp.server.dto.CompanyProfileDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;
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

@Service
public class AdminService {

    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    public AdminService(StudentRepository studentRepository,
                        CompanyRepository companyRepository,
                        OfferRepository offerRepository,
                        UserRepository userRepository,
                        ProfileMapper profileMapper) {
        this.studentRepository = studentRepository;
        this.companyRepository = companyRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
    }


    public List<StudentProfileDto> getAllStudents(String sessionId){

        SessionManager.requireAdmin(sessionId, userRepository);

        return studentRepository.findAll()
                .stream()
                .map(profileMapper::toStudentDto)
                .collect(Collectors.toList());
    }


    public List<CompanyProfileDto> getAllCompanies(String sessionId){

        SessionManager.requireAdmin(sessionId, userRepository);

        return companyRepository.findAll()
                .stream()
                .map(profileMapper::toCompanyDto)
                .collect(Collectors.toList());
    }


    public void deleteStudent(String sessionId, Long id){

        SessionManager.requireAdmin(sessionId, userRepository);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Estudiant no trobat"));

        studentRepository.delete(student);
    }


    public void deleteCompany(String sessionId, Long id){

        SessionManager.requireAdmin(sessionId, userRepository);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "Empresa no trobada"));

        companyRepository.delete(company);
    }


    public void deleteOffer(String sessionId, Long id){

        SessionManager.requireAdmin(sessionId, userRepository);

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST, "Oferta no trobada"));

        offerRepository.delete(offer);
    }
}