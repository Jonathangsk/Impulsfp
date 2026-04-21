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


/**
 * Controlador per gestionar les ofertes de feina; Proporciona endpoints per crear, obtenir, actualitzar i eliminar ofertes, així com per buscar ofertes per ubicació i modalitat.
 *
 * @author Jonathan Giraldo Giraldo
 *
 */
@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;
    private final OfferMapper offerMapper;

    public OfferController(OfferService offerService, OfferMapper offerMapper) {
        this.offerService = offerService;
        this.offerMapper = offerMapper;
    }


    /**
     * Endpoint per crear una nova oferta de feina. Requereix un sessionId per autenticar l'empresa que crea l'oferta i un CreateOfferDto amb la informació de l'oferta a crear.
     * @param sessionId
     * @param dto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createOffer(
            @RequestParam String sessionId,
            @RequestBody CreateOfferDto dto){

        offerService.createOffer(sessionId, dto);
        return ResponseEntity.ok(Map.of("message", "Oferta creada correctament"));
    }


    /**
     * Endpoint per obtenir les ofertes de feina publicades per l'empresa associada al sessionId proporcionat. Retorna una llista de les ofertes de l'empresa.
     * @param sessionId El sessionId de l'empresa de la qual es vol obtenir les ofertes publicades.
     * @return Una resposta HTTP amb una llista de les ofertes publicades per l'empresa associada al sessionId proporcionat, transformades a DTOs mitjançant el OfferMapper.
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyOffers(@RequestParam String sessionId){

        return ResponseEntity.ok(
                offerService.getMyOffers(sessionId)
                        .stream()
                        .map(offerMapper::toDto)
                        .toList()
        );
    }

    /**
     * Endpoint per obtenir els applicants d'una oferta de feina específica.
     * @param sessionId El sessionId de l'empresa que vol obtenir els applicants de l'oferta.
     * @param id L'id de l'oferta de la qual es vol obtenir els applicants, proporcionat com a paràmetre de la ruta.
     * @return Una resposta HTTP amb una llista dels applicants que han aplicat a l'oferta especificada, obtinguda mitjançant el servei OfferService.
     */
    @GetMapping("/{id}/applicants")
    public ResponseEntity<?> getApplicants(
            @RequestParam String sessionId,
            @PathVariable Long id){ //id de l'oferta de la qual es vol obtenir els applicants, proporcionat com a paràmetre de la ruta

        return ResponseEntity.ok(
                offerService.getApplicants(sessionId, id)
        );
    }


    /**
     * Endpoint per obtenir totes les ofertes de feina disponibles. Retorna una llista de totes les ofertes publicades, transformades a DTOs mitjançant el OfferMapper.
     * @return Una resposta HTTP amb una llista de totes les ofertes de feina disponibles, transformades a DTOs mitjançant el OfferMapper.
     */
    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(offerService.getAllOffers());
    }


    /**
     * Endpoint per buscar ofertes de feina per ubicació
     * @param location El paràmetre de consulta "location" que especifica la ubicació per la qual es vol buscar les ofertes de feina.
     * @return Una resposta HTTP amb una llista de les ofertes de feina que coincideixen amb la ubicació proporcionada, obtinguda mitjançant el servei OfferService.
     */
    @GetMapping("/search/location")
    public ResponseEntity<?> byLocation(@RequestParam String location){
        return ResponseEntity.ok(offerService.getOffersByLocation(location));
    }

    /**
     * Endpoint per buscar ofertes de feina per modalitat.
     * @param modality El paràmetre de consulta "modality" que especifica la modalitat per la qual es vol buscar las ofertes de feina (per exemple, "presencial", "remoto", "híbrido").
     * @return Una resposta HTTP amb una llista de las ofertes de feina que coincideixen con la modalitat proporcionada, obtinguda mitjançant el servei OfferService.
     */
    @GetMapping("/search/modality")
    public ResponseEntity<?> byModality(@RequestParam String modality){
        return ResponseEntity.ok(offerService.getOffersByModality(modality));
    }

    /**
     * Endpoint per eliminar una oferta de feina específica.
     * @param sessionId El sessionId de l'empresa que vol eliminar la oferta de feina.
     * @param id El id de la oferta de feina que se vol eliminar, proporcionado como paràmetre de la ruta.
     * @return Una resposta HTTP amb un mensaje de confirmación indicando que la oferta se ha eliminado correctamente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffer(
            @RequestParam String sessionId,
            @PathVariable Long id){

        offerService.deleteOffer(sessionId, id);

        return ResponseEntity.ok(Map.of("message", "Oferta eliminada correctament"));
    }

    /**
     * Endpoint per actualizar una oferta de feina específica.
     * @param sessionId El sessionId de l'empresa que vol actualizar la oferta de feina.
     * @param id El id de la oferta de feina que se vol actualizar, proporcionado como paràmetre de la ruta.
     * @param dto Un UpdateOfferDto que conté la información actualizada de la oferta de feina
     * @return Una resposta HTTP amb un missatge de confirmació indicar que l'oferta s'actualitzat correctament
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOffer(
            @RequestParam String sessionId,
            @PathVariable Long id,
            @RequestBody UpdateOfferDto dto){

        offerService.updateOffer(sessionId, id, dto);

        return ResponseEntity.ok(Map.of("message", "Oferta actualitzada correctament"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOfferById(@PathVariable Long id){
        return ResponseEntity.ok(offerService.getOfferById(id));
    }


}