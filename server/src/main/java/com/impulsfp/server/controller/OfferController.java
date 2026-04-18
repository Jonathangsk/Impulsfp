package com.impulsfp.server.controller;

import com.impulsfp.server.dto.CreateOfferDto;
import com.impulsfp.server.dto.UpdateOfferDto;
import com.impulsfp.server.mapper.OfferMapper;
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
    private final OfferMapper offerMapper;

    public OfferController(OfferService offerService, OfferMapper offerMapper) {
        this.offerService = offerService;
        this.offerMapper = offerMapper;
    }



    @PostMapping
    public ResponseEntity<?> createOffer(
            @RequestParam String sessionId,
            @RequestBody CreateOfferDto dto){

        offerService.createOffer(sessionId, dto);
        return ResponseEntity.ok(Map.of("message", "Oferta creada correctament"));
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
        return ResponseEntity.ok(Map.of("message", "Aplicació enviada correctament"));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyOffers(@RequestParam String sessionId){

        return ResponseEntity.ok(
                offerService.getMyOffers(sessionId)
                        .stream()
                        .map(offerMapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/{id}/applicants")
    public ResponseEntity<?> getApplicants(
            @RequestParam String sessionId,
            @PathVariable Long id){

        return ResponseEntity.ok(
                offerService.getApplicants(sessionId, id)
        );
    }



    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/search/location")
    public ResponseEntity<?> byLocation(@RequestParam String location){
        return ResponseEntity.ok(offerService.getOffersByLocation(location));
    }

    @GetMapping("/search/modality")
    public ResponseEntity<?> byModality(@RequestParam String modality){
        return ResponseEntity.ok(offerService.getOffersByModality(modality));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffer(
            @RequestParam String sessionId,
            @PathVariable Long id){

        offerService.deleteOffer(sessionId, id);

        return ResponseEntity.ok(Map.of("message", "Oferta eliminada correctament"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOffer(
            @RequestParam String sessionId,
            @PathVariable Long id,
            @RequestBody UpdateOfferDto dto){

        offerService.updateOffer(sessionId, id, dto);

        return ResponseEntity.ok(Map.of("message", "Oferta actualitzada correctament"));
    }


}