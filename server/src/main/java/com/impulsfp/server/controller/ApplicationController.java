package com.impulsfp.server.controller;

import com.impulsfp.server.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<?> apply(
            @RequestParam String sessionId,
            @PathVariable Long id){

        applicationService.apply(sessionId, id);

        return ResponseEntity.ok(Map.of("message", "Aplicació enviada"));
    }

    @GetMapping("/my")
    public ResponseEntity<?> my(@RequestParam String sessionId){
        return ResponseEntity.ok(applicationService.getMyApplications(sessionId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestParam String sessionId,
            @PathVariable Long id,
            @RequestBody Map<String, String> body){

        applicationService.updateStatus(sessionId, id, body.get("status"));

        return ResponseEntity.ok(Map.of("message", "Actualitzat"));
    }
}