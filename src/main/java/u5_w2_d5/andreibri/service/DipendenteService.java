package u5_w2_d5.andreibri.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import u5_w2_d5.andreibri.entities.Dipendente;
import u5_w2_d5.andreibri.entities.Viaggio;
import u5_w2_d5.andreibri.exception.ConflictException;
import u5_w2_d5.andreibri.exception.NotFoundException;
import u5_w2_d5.andreibri.payloads.DipendenteRequestDTO;
import u5_w2_d5.andreibri.payloads.DipendenteResponseDTO;
import u5_w2_d5.andreibri.repositories.DipendenteRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
public class DipendenteService {

    private final DipendenteRepository dipendenteRepository;
    private final ViaggioService viaggioService;
    @Autowired
    private CloudinaryService cloudinaryService;

    public List<DipendenteResponseDTO> findAll() {
        return dipendenteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public DipendenteResponseDTO findById(Long id) {
        Dipendente d = dipendenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dipendente con id " + id + " non trovato"));
        return toDTO(d);
    }

    public DipendenteResponseDTO create(DipendenteRequestDTO dto, MultipartFile file) throws IOException {

        if (dipendenteRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("Username '" + dto.getUsername() + "' già in uso");
        }
        if (dipendenteRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Email '" + dto.getEmail() + "' già registrata");
        }

        Dipendente d = new Dipendente();
        d.setUsername(dto.getUsername());
        d.setNome(dto.getNome());
        d.setCognome(dto.getCognome());
        d.setEmail(dto.getEmail());
        d.setPassword(dto.getPassword());

        if (file != null && !file.isEmpty()) {

            String avatarUrl = cloudinaryService.upload(file);
            d.setAvatar(avatarUrl);
        } else {

            String placeholder = "https://ui-avatars.com/api/?name=" + d.getNome() + "+" + d.getCognome();
            d.setAvatar(placeholder);
        }

        return toDTO(dipendenteRepository.save(d));
    }


    public DipendenteResponseDTO update(Long id, DipendenteRequestDTO dto) {
        Dipendente d = dipendenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dipendente con id " + id + " non trovato"));

        // Controlla username duplicato escludendo se stesso
        dipendenteRepository.findByUsername(dto.getUsername())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Username già in uso");
                });

        dipendenteRepository.findByEmail(dto.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Email già registrata");
                });

        d.setUsername(dto.getUsername());
        d.setNome(dto.getNome());
        d.setCognome(dto.getCognome());
        d.setEmail(dto.getEmail());
        d.setAvatar("https://ui-avatars.com/api/?name="
                + d.getNome() + "+" + d.getCognome());
        return toDTO(dipendenteRepository.save(d));
    }

    public DipendenteResponseDTO uploadAvatar(Long id, MultipartFile file) throws IOException {

        Dipendente d = dipendenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dipendente con id " + id + " non trovato"));


        if (file == null || file.isEmpty()) {
            throw new u5_w2_d5.andreibri.exception.BadRequestException("File mancante: devi allegare un'immagine");
        }

        String avatarUrl = cloudinaryService.upload(file);

        d.setAvatar(avatarUrl);
        return toDTO(dipendenteRepository.save(d));
    }


    public void delete(Long id) {
        if (!dipendenteRepository.existsById(id)) {
            throw new NotFoundException("Dipendente con id " + id + " non trovato");
        }
        dipendenteRepository.deleteById(id);
    }

    public Dipendente findEntityById(Long id) {
        return dipendenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dipendente con id " + id + " non trovato"));
    }

    // Assegnazione di un viaggio a un dipendente
    public DipendenteResponseDTO assegnaViaggio(Long dipendenteId, Long viaggioId) {
        Dipendente d = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new NotFoundException("Dipendente con id " + dipendenteId + " non trovato"));
        Viaggio v = viaggioService.getEntityById(viaggioId);

        boolean giaAssegnato = d.getViaggi().stream()
                .anyMatch(viaggio -> viaggio.getId().equals(viaggioId));

        if (giaAssegnato) {
            throw new ConflictException("Il viaggio è già assegnato a questo dipendente");
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
        dto.setAvatar(d.getAvatar());

        dto.setViaggi(
                d.getViaggi().stream()
                        .map(viaggioService::toDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
