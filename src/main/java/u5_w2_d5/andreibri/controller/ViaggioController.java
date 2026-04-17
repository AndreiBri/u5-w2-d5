package u5_w2_d5.andreibri.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import u5_w2_d5.andreibri.payloads.StatoUpdateDTO;
import u5_w2_d5.andreibri.payloads.ViaggioRequestDTO;
import u5_w2_d5.andreibri.payloads.ViaggioResponseDTO;
import u5_w2_d5.andreibri.service.ViaggioService;


import java.util.List;

@RestController
@RequestMapping("/api/viaggi")
@RequiredArgsConstructor
public class ViaggioController {

    private final ViaggioService viaggioService;

    // GET /api/viaggi -> lista tutti i viaggi
    @GetMapping
    public ResponseEntity<List<ViaggioResponseDTO>> getAll() {
        return ResponseEntity.ok(viaggioService.findAll());
    }

    // GET /api/viaggi/{id} -> dettaglio viaggio
    @GetMapping("/{id}")
    public ResponseEntity<ViaggioResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(viaggioService.findById(id));
    }

    // POST /api/viaggi -> crea nuovo viaggio
    @PostMapping
    public ResponseEntity<ViaggioResponseDTO> create(@Valid @RequestBody ViaggioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(viaggioService.create(dto));
    }

    // PUT /api/viaggi/{id} -> aggiorna viaggio completo
    @PutMapping("/{id}")
    public ResponseEntity<ViaggioResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ViaggioRequestDTO dto) {
        return ResponseEntity.ok(viaggioService.update(id, dto));
    }

    // PATCH /api/viaggi/{id}/stato -> aggiorna solo lo stato
    @PatchMapping("/{id}/stato")
    public ResponseEntity<ViaggioResponseDTO> updateStato(
            @PathVariable Long id,
            @Valid @RequestBody StatoUpdateDTO dto) {
        return ResponseEntity.ok(viaggioService.updateStato(id, dto));
    }

    // DELETE /api/viaggi/{id} -> elimina viaggio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viaggioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
