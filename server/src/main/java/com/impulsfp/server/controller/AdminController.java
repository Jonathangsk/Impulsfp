package com.impulsfp.server.controller;

import com.impulsfp.server.dto.CompanyProfileDto;
import com.impulsfp.server.dto.StudentProfileDto;
import com.impulsfp.server.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador per a les operacions d'administració
 * Aquest controlador permet als administradors obtenir llistes d'estudiants i empreses, així com eliminar estudiants, empreses i ofertes. També permet obtenir totes les ofertes disponibles.
 *
 *
 * @author Jonathan Giraldo Giraldo
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    /**
     * Endpoint per obtenir la llista de tots els estudiants registrats. Requereix un sessionId vàlid per autenticar l'administrador.
     * @param sessionId El sessionId de l'administrador que sol·licita la llista d'estudiants.
     * @return StudentProfileDto que representen els estudiants registrats
     */
    @GetMapping("/students")
    public ResponseEntity<List<StudentProfileDto>> getAllStudents(@RequestParam String sessionId){
        return ResponseEntity.ok(adminService.getAllStudents(sessionId));
    }

    /**
     * Endpoint per obtenir la llista de totes les empreses registrades. Requereix un sessionId vàlid per autenticar l'administrador.
     * @param sessionId El sessionId de l'administrador que sol·licita la llista d'empreses.
     * @return CompanyProfileDto que representen les empreses registrades
     */
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyProfileDto>> getAllCompanies(@RequestParam String sessionId){
        return ResponseEntity.ok(adminService.getAllCompanies(sessionId));
    }

    /**
     * Endpoint per eliminar un estudiant registrat. Requereix un sessionId vàlid per autenticar l'administrador i l'id de l'estudiant que es vol eliminar.
     * @param id L'id de l'estudiant que es vol eliminar.
     * @param sessionId El sessionId de l'administrador que sol·licita l'eliminació de l'estudiant.
     * @return Un missatge de confirmació que indica que l'estudiant ha estat eliminat correctament.
     */
    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id,
                                           @RequestParam String sessionId){
        adminService.deleteStudent(sessionId, id);
        return ResponseEntity.ok(Map.of("message", "Estudiant eliminat"));
    }

    /**
     * Endpoint per eliminar una empresa registrada. Requereix un sessionId vàlid per autenticar l'administrador i l'id de l'empresa que es vol eliminar.
     * @param id L'id de l'empresa que es vol eliminar.
     * @param sessionId El sessionId de l'administrador que sol·licita l'eliminació de l'empresa.
     * @return Un missatge de confirmació que indica que l'empresa ha estat eliminada correctament.
     */
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id,
                                           @RequestParam String sessionId){
        adminService.deleteCompany(sessionId, id);
        return ResponseEntity.ok(Map.of("message", "Empresa eliminada"));
    }

    /**
     * Endpoint per eliminar una oferta registrada. Requereix un sessionId vàlid per autenticar l'administrador i l'id de l'oferta que es vol eliminar.
     * @param id L'id de l'oferta que es vol eliminar.
     * @param sessionId El sessionId de l'administrador que sol·licita l'eliminació de l'oferta.
     * @return Un missatge de confirmació que indica que l'oferta ha estat eliminada correctament.
     */
    @DeleteMapping("/offers/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id,
                                         @RequestParam String sessionId){
        adminService.deleteOffer(sessionId, id);
        return ResponseEntity.ok(Map.of("message", "Oferta eliminada"));
    }

    /**
     * Endpoint per obtenir la llista de totes les ofertes registrades. Requereix un sessionId vàlid per autenticar l'administrador.
     * @param sessionId El sessionId de l'administrador que sol·licita la llista d'ofertes.
     * @return Una llista d'ofertes registrades a la plataforma, amb informació detallada sobre cada oferta, com el títol, la descripció, els requisits i la data de publicació.
     */
    @GetMapping("/offers")
    public ResponseEntity<?> getAllOffers(@RequestParam String sessionId){
        return ResponseEntity.ok(adminService.getAllOffers(sessionId));
    }


}