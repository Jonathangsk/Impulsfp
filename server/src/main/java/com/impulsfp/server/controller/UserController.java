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
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteAccount(@RequestParam String sessionId){

        userService.deleteAccount(sessionId);
        return ResponseEntity.ok(Map.of("message", "Compte eliminat correctament"));

    }
}