package com.impulsfp.server.controller;

import com.impulsfp.server.dto.CompanyProfileDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 🔵 GET STUDENTS
    @GetMapping("/students")
    public ResponseEntity<List<StudentProfileDto>> getAllStudents(@RequestParam String sessionId){
        return ResponseEntity.ok(adminService.getAllStudents(sessionId));
    }

    // 🟢 GET COMPANIES
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyProfileDto>> getAllCompanies(@RequestParam String sessionId){
        return ResponseEntity.ok(adminService.getAllCompanies(sessionId));
    }

    // 🔴 DELETE STUDENT
    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id,
                                           @RequestParam String sessionId){
        adminService.deleteStudent(sessionId, id);
        return ResponseEntity.ok(Map.of("message", "Estudiant eliminat"));
    }

    // 🟠 DELETE COMPANY
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id,
                                           @RequestParam String sessionId){
        adminService.deleteCompany(sessionId, id);
        return ResponseEntity.ok(Map.of("message", "Empresa eliminada"));
    }

    // 🟡 DELETE OFFER
    @DeleteMapping("/offers/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id,
                                         @RequestParam String sessionId){
        adminService.deleteOffer(sessionId, id);
        return ResponseEntity.ok(Map.of("message", "Oferta eliminada"));
    }
}