package com.impulsfp.server.controller;

import com.impulsfp.server.dto.CreateOfferDto;
import com.impulsfp.server.model.Offer;
import com.impulsfp.server.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<?> createOffer(
            @RequestParam String sessionId,
            @RequestBody CreateOfferDto dto){

        offerService.createOffer(sessionId, dto);
        return ResponseEntity.ok(Map.of("message", "Oferta creada"));
    }

    /**
     *
     * @param sessionId
     * @param id
     * @return
     */
    @PostMapping("/{id}/apply")
    public ResponseEntity<?> apply(
            @RequestParam String sessionId,
            @PathVariable Long id){

        offerService.applyToOffer(sessionId, id);
        return ResponseEntity.ok(Map.of("message", "Aplicació enviada"));
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getAll(){
        return ResponseEntity.ok(offerService.getAllOffers());
    }
}