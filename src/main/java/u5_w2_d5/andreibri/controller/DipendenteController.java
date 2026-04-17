package u5_w2_d5.andreibri.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import u5_w2_d5.andreibri.payloads.DipendenteRequestDTO;
import u5_w2_d5.andreibri.payloads.DipendenteResponseDTO;
import u5_w2_d5.andreibri.service.DipendenteService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dipendenti")
@RequiredArgsConstructor
public class DipendenteController {

    private final DipendenteService dipendenteService;

    // GET /api/dipendenti -> lista tutti i dipendenti
    @GetMapping
    public ResponseEntity<List<DipendenteResponseDTO>> getAll() {
        return ResponseEntity.ok(dipendenteService.findAll());
    }

    // GET /api/dipendenti/{id} -> dettaglio dipendente
    @GetMapping("/{id}")
    public ResponseEntity<DipendenteResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dipendenteService.findById(id));
    }

    // POST /api/dipendenti -> crea nuovo dipendente
    @PostMapping
    public ResponseEntity<DipendenteResponseDTO> create(@Valid @RequestBody DipendenteRequestDTO dto) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(dipendenteService.create(dto, null));
    }

    // PUT /api/dipendenti/{id} -> aggiorna dipendente
    @PutMapping("/{id}")
    public ResponseEntity<DipendenteResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DipendenteRequestDTO dto) {
        return ResponseEntity.ok(dipendenteService.update(id, dto));
    }

    // PATCH /api/dipendenti/{id}/avatar -> aggiorna dipendente avatar
    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DipendenteResponseDTO uploadAvatar(
            @PathVariable Long id,
            @RequestPart(value = "avatar", required = false) MultipartFile file) throws IOException {

        return dipendenteService.uploadAvatar(id, file);
    }

    // DELETE /api/dipendenti/{id} -> elimina dipendente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dipendenteService.delete(id);
        return ResponseEntity.ok().build();
    }
}
