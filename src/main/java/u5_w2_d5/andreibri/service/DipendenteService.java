package u5_w2_d5.andreibri.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import u5_w2_d5.andreibri.entities.Dipendente;
import u5_w2_d5.andreibri.entities.Viaggio;
import u5_w2_d5.andreibri.payloads.DipendenteRequestDTO;
import u5_w2_d5.andreibri.payloads.DipendenteResponseDTO;
import u5_w2_d5.andreibri.repositories.DipendenteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DipendenteService {

    private final DipendenteRepository dipendenteRepository;
    private final ViaggioService viaggioService;

    public List<DipendenteResponseDTO> findAll() {
        return dipendenteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DipendenteResponseDTO findById(Long id) {
        Dipendente d = dipendenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dipendente con id non trovato"));
        return toDTO(d);
    }

    public DipendenteResponseDTO create(DipendenteRequestDTO dto) {
        if (dipendenteRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Dipende con username giá in uso"); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION
        }
        if (dipendenteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Dipendente con email giá in uso"); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION
        }
        Dipendente d = new Dipendente();
        d.setUsername(dto.getUsername());
        d.setNome(dto.getNome());
        d.setCognome(dto.getCognome());
        d.setEmail(dto.getEmail());
        return toDTO(dipendenteRepository.save(d));
    }

    public DipendenteResponseDTO update(Long id, DipendenteRequestDTO dto) {
        Dipendente d = dipendenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dipendente con id: " + id + " non trovato")); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION

        // Controlla username duplicato escludendo se stesso
        dipendenteRepository.findByUsername(dto.getUsername())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Username già in uso");
                }); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION

        dipendenteRepository.findByEmail(dto.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Email già registrata");
                }); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION

        d.setUsername(dto.getUsername());
        d.setNome(dto.getNome());
        d.setCognome(dto.getCognome());
        d.setEmail(dto.getEmail());
        return toDTO(dipendenteRepository.save(d));
    }

    public void delete(Long id) {
        if (dipendenteRepository.existsById(id)) {
            throw new RuntimeException("Dipendente con id: " + id + " non trovato"); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION
        }
        dipendenteRepository.deleteById(id);
    }

    // Assegnazione di un viaggio a un dipendente
    public DipendenteResponseDTO assegnaViaggio(Long dipendenteId, Long viaggioId) {
        Dipendente d = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new RuntimeException("Dipendente non trovato")); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION
        Viaggio v = viaggioService.getEntityById(viaggioId);

        boolean giaAssegnato = d.getViaggi().stream()
                .anyMatch(viaggio -> viaggio.getId().equals(viaggioId));

        if (giaAssegnato) {
            throw new RuntimeException("Viaggio non trovato"); // DA CAMBIARE DOPO CON CUSTOM EXCEPTION
        }

        d.getViaggi().add(v);
        return toDTO(dipendenteRepository.save(d));
    }

    // Mapper entity -> DTO
    public DipendenteResponseDTO toDTO(Dipendente d) {
        DipendenteResponseDTO dto = new DipendenteResponseDTO();
        dto.setId(d.getId());
        dto.setUsername(d.getUsername());
        dto.setNome(d.getNome());
        dto.setCognome(d.getCognome());
        dto.setEmail(d.getEmail());
        dto.setViaggi(
                d.getViaggi().stream()
                        .map(viaggioService::toDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
