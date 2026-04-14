package com.impulsfp.server.controller;

import com.impulsfp.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador per gestionar les operacions relacionades amb els usuaris, com l'eliminació de comptes, modificació de dades personals, etc.
 * Aquest controlador utilitza el servei UserService per realitzar les operacions necessàries i retorna respostes adequades als clients.
 *
 * @author Jonathan Giraldo Giraldo
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint per eliminar el compte d'un usuari; rep un identificador de sessió com a paràmetre i elimina el compte associat a aquesta sessió.
     * *
     * @param sessionId
     * @return ResponseEntity amb un missatge de confirmació si el compte s'ha eliminat correctament, o un error 401 si el sessionId no és vàlid
     */

    /**
     * Endpoint per eliminar el compte d'un usuari; rep un identificador de sessió com a paràmetre i una contrasenya al cos de la petició, i elimina el compte associat a aquesta sessió si la contrasenya és correcta.
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar l'usuari que vol eliminar el seu compte, proporcionat com a paràmetre de la petició
     * @param body objecte JSON que conté la contrasenya de l'usuari, proporcionat al cos de la petició; s'espera que el JSON tingui un camp "password" amb la contrasenya de l'usuari
     * @return ResponseEntity amb un missatge de confirmació si el compte s'ha eliminat correctament, o un error 401 si el sessionId no és vàlid o la contrasenya és incorrecta
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteAccount(
            @RequestParam String sessionId,
            @RequestBody Map<String, String> body){ //map que representa el cos de la petició, esperant un camp "password" amb la contrasenya de l'usuari

        userService.deleteAccount(sessionId, body.get("password"));

        return ResponseEntity.ok(Map.of("message", "Compte eliminat correctament"));
    }

    /**
     * Endpoint per obtenir el perfil de l'usuari associat a la sessió actual
     * @param sessionId identificador de sessió que s'ha d'utilitzar per identificar l'usuari del qual es vol obtenir el perfil, proporcionat com a paràmetre de la petició
     * @return ResponseEntity amb les dades del perfil de l'usuari si el sessionId és vàlid, o un error 401 si el sessionId no és vàlid
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@RequestParam String sessionId) {
        return ResponseEntity.ok(userService.getMyProfile(sessionId));
    }

}