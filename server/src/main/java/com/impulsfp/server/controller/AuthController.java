package com.impulsfp.server.controller;

import com.impulsfp.server.dto.LoginRequestDto;
import com.impulsfp.server.dto.LoginResponseDto;
import com.impulsfp.server.service.AuthService;
import com.impulsfp.server.session.SessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador REST que gestiona les peticions d'autenticació dels usuaris; proporciona la lògica i els endpoints per iniciar i finalitzar sessió.+
 *
 * @author Jonathan Giraldo Giraldo
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint per iniciar sessió; rep un objecte JSON amb el nom d'usuari i la contrasenya, i retorna un objecte JSON
     * amb l'identificador de sessió i el tipus d'usuari si les credencials són correctes. Si no, retorna un error 401
     * @param request objecte DTO que representa les dades de la petició d'inici de sessió, amb camps per a nom d'usuari i contrasenya
     * @return ResponseEntity amb les dades de resposta o un error 401 si les credencials no són correctes
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request){

        LoginResponseDto response = authService.login(request.getUsername(), request.getPassword()); //el servei d'autenticació verifica les credencials i retorna un objecte de resposta amb les dades de sessió (sessionId i userType) si són correctes
        if(response == null){ //si les credencials no són correctes, el servei retorna null
            return ResponseEntity.status(401)
                    .body("{\"error\":\"Usuari o contrasenya incorrectes\"}");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint per finalitzar sessió; rep un identificador de sessió com a paràmetre i elimina la sessió corresponent. Si el
     * identificador de sessió no és vàlid, retorna un error 401
     * @param sessionId identificador de sessió que s'ha de finalitzar, proporcionat com a paràmetre de la petició
     * @return ResponseEntity amb un missatge de confirmació si la sessió s'ha finalitzat correctament, o un error 401 si el sessionId no és vàlid
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String sessionId){

        if(!SessionManager.isValid(sessionId)){ //verifica que el sessionId proporcionat és vàlid; si no ho és, retorna un error 401
            return ResponseEntity.status(401)
                    .body("{\"error\":\"Sessió no vàlida\"}");
        }

        SessionManager.removeSession(sessionId); //si el sessionId és vàlid, elimina la sessió corresponent i retorna un missatge de confirmació

        return ResponseEntity.ok("{\"message\":\"Sessió finalitzada correctament\"}");
    }

    /**
     * Endpoint per gestionar les peticions GET a /auth/login; informa que encara no accepta peticions GET
     * @return amb un missatge d'error en format JSON indicant que aquest endpoint no accepta peticions GET; és merament informatiu i no té cap funcionalitat real
     */
    @GetMapping("/login")
    public String missatgeErrorGet(){
        return "{\"error\":\"Aquest endpoint no accepta peticions GET\"}";
    }
}