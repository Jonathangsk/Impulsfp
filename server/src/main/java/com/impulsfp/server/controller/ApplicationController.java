package com.impulsfp.server.controller;

import com.impulsfp.server.dto.ApplyDto;
import com.impulsfp.server.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * controlador per a les aplicacions a les ofertes. Permet als estudiants aplicar-se a les ofertes i veure les seves aplicacions, i als centres actualitzar l'estat de les aplicacions.
 * Aquest controlador utilitza el servei ApplicationService per realitzar les operacions necessàries i retorna respostes adequades als clients.
 *
 * @author Jonathan Giraldo Giraldo
 */

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Endpoint per aplicar-se a una oferta
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar l'estudiant que vol aplicar-se a l'oferta, proporcionat com a paràmetre de la petició
     * @return ResponseEntity amb un missatge de confirmació si l'aplicació s'ha creat correctament, o un error 401 si el sessionId no és vàlid o l'oferta no existeix
     */
    @PostMapping("/apply")
    public ResponseEntity<?> apply(
            @RequestParam String sessionId,
            @RequestBody ApplyDto dto){

        applicationService.apply(sessionId, dto);
        return ResponseEntity.ok(Map.of("message", "Aplicació enviada correctament"));
    }


    /**
     * Endpoint per obtenir les aplicacions de l'estudiant associat a la sessió actual
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar l'estudiant del qual es vol obtenir les aplicacions, proporcionat com a paràmetre de la petició
     * @return ResponseEntity amb una llista de les aplicacions de l'estudiant si el sessionId és vàlid, o un error 401 si el sessionId no és vàlid
     */
    @GetMapping("/my")
    public ResponseEntity<?> my(@RequestParam String sessionId){
        return ResponseEntity.ok(applicationService.getMyApplications(sessionId));
    }


    /**
     * Endpoint per actualitzar l'estat d'una aplicació
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar el centre que vol actualitzar l'estat de l'aplicació, proporcionat com a paràmetre de la petició
     * @param id identificador de l'aplicació que es vol actualitzar, proporcionat com a paràmetre de la petició
     * @param body objecte JSON que conté l'estat al qual es vol actualitzar l'aplicació, proporcionat al cos de la petició; s'espera que el JSON tingui un camp "status" amb el nou estat de l'aplicació
     * @return ResponseEntity amb un missatge de confirmació si l'estat de l'aplicació s'ha actualitzat correctament, o un error 401 si el sessionId no és vàlid o l'aplicació no existeix
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestParam String sessionId,
            @PathVariable Long id,
            @RequestBody Map<String, String> body){

        applicationService.updateStatus(sessionId, id, body.get("status"));

        return ResponseEntity.ok(Map.of("message", "Actualitzat"));
    }
}