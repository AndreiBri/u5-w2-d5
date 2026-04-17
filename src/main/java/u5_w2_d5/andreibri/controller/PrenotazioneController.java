package u5_w2_d5.andreibri.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import u5_w2_d5.andreibri.payloads.PrenotazioneRequestDTO;
import u5_w2_d5.andreibri.payloads.PrenotazioneResponseDTO;
import u5_w2_d5.andreibri.service.PrenotazioneService;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    // GET /api/prenotazioni → lista tutte le prenotazioni
    @GetMapping
    public ResponseEntity<List<PrenotazioneResponseDTO>> getAll() {
        return ResponseEntity.ok(prenotazioneService.findAll());
    }

    // GET /api/prenotazioni/{id} → dettaglio prenotazione
    @GetMapping("/{id}")
    public ResponseEntity<PrenotazioneResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prenotazioneService.findById(id));
    }

    // GET /api/prenotazioni/dipendente/{dipendenteId} → prenotazioni per dipendente
    @GetMapping("/dipendente/{dipendenteId}")
    public ResponseEntity<List<PrenotazioneResponseDTO>> getByDipendente(
            @PathVariable Long dipendenteId) {
        return ResponseEntity.ok(prenotazioneService.findByDipendente(dipendenteId));
    }

    // POST /api/prenotazioni → crea nuova prenotazione
    @PostMapping
    public ResponseEntity<PrenotazioneResponseDTO> create(
            @Valid @RequestBody PrenotazioneRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prenotazioneService.create(dto));
    }

    // PUT /api/prenotazioni/{id} → aggiorna prenotazione
    @PutMapping("/{id}")
    public ResponseEntity<PrenotazioneResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PrenotazioneRequestDTO dto) {
        return ResponseEntity.ok(prenotazioneService.update(id, dto));
    }

    // DELETE /api/prenotazioni/{id} → elimina prenotazione
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prenotazioneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}