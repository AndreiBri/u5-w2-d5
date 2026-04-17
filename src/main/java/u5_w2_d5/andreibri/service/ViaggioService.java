package u5_w2_d5.andreibri.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import u5_w2_d5.andreibri.entities.Viaggio;
import u5_w2_d5.andreibri.enums.StatoViaggio;
import u5_w2_d5.andreibri.exception.BadRequestException;
import u5_w2_d5.andreibri.exception.NotFoundException;
import u5_w2_d5.andreibri.payloads.StatoUpdateDTO;
import u5_w2_d5.andreibri.payloads.ViaggioRequestDTO;
import u5_w2_d5.andreibri.payloads.ViaggioResponseDTO;
import u5_w2_d5.andreibri.repositories.ViaggioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViaggioService {

    private final ViaggioRepository viaggioRepository;

    public List<ViaggioResponseDTO> findAll() {
        return viaggioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ViaggioResponseDTO findById(Long id) {
        Viaggio viaggio = viaggioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Viaggio con id " + id + " non trovato"));
        return toDTO(viaggio);
    }

    public Viaggio getEntityById(Long id) {
        return viaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Errore nel ID del Viaggio "));// DA CAMBIARE DOPO CON CUSTOM EXCEPTION
    }

    public ViaggioResponseDTO create(ViaggioRequestDTO dto) {
        StatoViaggio stato = parseStato(dto.getStato());
        Viaggio viaggio = new Viaggio();
        viaggio.setDestinazione(dto.getDestinazione());
        viaggio.setData(dto.getData());
        viaggio.setStato(parseStato(dto.getStato()));
        return toDTO(viaggioRepository.save(viaggio));
    }

    public ViaggioResponseDTO update(Long id, ViaggioRequestDTO dto) {

        Viaggio viaggio = viaggioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Viaggio con id " + id + " non trovato"));
        viaggio.setData(dto.getData());
        viaggio.setStato(parseStato(dto.getStato()));
        return toDTO(viaggioRepository.save(viaggio));
    }

    public ViaggioResponseDTO updateStato(Long id, StatoUpdateDTO dto) {
        Viaggio viaggio = viaggioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Viaggio con id " + id + " non trovato"));
        viaggio.setStato(parseStato(dto.getStato()));
        return toDTO(viaggioRepository.save(viaggio));
    }

    public void delete(Long id) {
        if (viaggioRepository.existsById(id)) {
            throw new NotFoundException("Viaggio con id " + id + " non trovato");
        }
        viaggioRepository.deleteById(id);
    }

    // Map per entity -> DTO
    public ViaggioResponseDTO toDTO(Viaggio viaggio) {
        ViaggioResponseDTO dto = new ViaggioResponseDTO();
        dto.setId(viaggio.getId());
        dto.setDestinazione(viaggio.getDestinazione());
        dto.setData(viaggio.getData());
        dto.setStato(viaggio.getStato().name());
        return dto;
    }

    // Stato Accertamento Validitá
    private StatoViaggio parseStato(String stato) {
        try {
            return StatoViaggio.valueOf(stato.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Stato non valido: '" + stato + "'. Valori accettati: IN_PROGRAMMA, COMPLETATO");
        }
    }
}
