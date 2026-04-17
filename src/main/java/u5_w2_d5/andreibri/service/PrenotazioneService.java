package u5_w2_d5.andreibri.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import u5_w2_d5.andreibri.entities.Dipendente;
import u5_w2_d5.andreibri.entities.Prenotazione;
import u5_w2_d5.andreibri.entities.Viaggio;
import u5_w2_d5.andreibri.exception.ConflictException;
import u5_w2_d5.andreibri.exception.NotFoundException;
import u5_w2_d5.andreibri.payloads.PrenotazioneRequestDTO;
import u5_w2_d5.andreibri.payloads.PrenotazioneResponseDTO;
import u5_w2_d5.andreibri.repositories.PrenotazioneRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final DipendenteService dipendenteService;
    private final ViaggioService viaggioService;

    // GET ALL
    public List<PrenotazioneResponseDTO> findAll() {
        return prenotazioneRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public PrenotazioneResponseDTO findById(Long id) {
        Prenotazione p = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione con id " + id + " non trovata"));
        return toDTO(p);
    }

    // GET BY DIPENDENTE
    public List<PrenotazioneResponseDTO> findByDipendente(Long dipendenteId) {
        // Verifichiamo che il dipendente esista
        dipendenteService.findById(dipendenteId);

        return prenotazioneRepository.findByDipendenteId(dipendenteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // CREATE
    @Transactional
    public PrenotazioneResponseDTO create(PrenotazioneRequestDTO dto) {
        Dipendente d = dipendenteService.findEntityById(dto.getDipendenteId());
        Viaggio v = viaggioService.getEntityById(dto.getViaggioId());

        // Regola: Un dipendente non può avere due viaggi nella stessa data
        checkConflittoData(d.getId(), v.getData());

        Prenotazione p = new Prenotazione();
        p.setDipendente(d);
        p.setViaggio(v);
        p.setDataRichiesta(dto.getDataRichiesta());
        p.setNote(dto.getNote());

        return toDTO(prenotazioneRepository.save(p));
    }

    // UPDATE
    @Transactional
    public PrenotazioneResponseDTO update(Long id, PrenotazioneRequestDTO dto) {
        Prenotazione p = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione con id " + id + " non trovata"));

        Dipendente d = dipendenteService.findEntityById(dto.getDipendenteId());
        Viaggio v = viaggioService.getEntityById(dto.getViaggioId());

        // Controllo conflitto solo se è cambiato il dipendente o la data del viaggio
        if (!p.getDipendente().getId().equals(d.getId()) || !p.getViaggio().getData().equals(v.getData())) {
            checkConflittoData(d.getId(), v.getData());
        }

        p.setDipendente(d);
        p.setViaggio(v);
        p.setDataRichiesta(dto.getDataRichiesta());
        p.setNote(dto.getNote());

        return toDTO(prenotazioneRepository.save(p));
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!prenotazioneRepository.existsById(id)) {
            throw new NotFoundException("Prenotazione con id " + id + " non trovata");
        }
        prenotazioneRepository.deleteById(id);
    }

    // Metodo di utility per il controllo date
    private void checkConflittoData(Long dipendenteId, java.time.LocalDate dataViaggio) {
        boolean giaImpegnato = prenotazioneRepository.existsByDipendenteIdAndDataRichiesta(dipendenteId, dataViaggio);
        if (giaImpegnato) {
            throw new ConflictException("Il dipendente è già impegnato in un altro viaggio per la data: " + dataViaggio);
        }
    }

    // Mapper Entity -> DTO
    public PrenotazioneResponseDTO toDTO(Prenotazione p) {
        PrenotazioneResponseDTO dto = new PrenotazioneResponseDTO();
        dto.setId(p.getId());
        dto.setViaggio(viaggioService.toDTO(p.getViaggio()));
        dto.setDipendenteId(p.getDipendente().getId());
        dto.setDipendenteUsername(p.getDipendente().getUsername());
        dto.setDataRichiesta(p.getDataRichiesta());
        dto.setNote(p.getNote());
        return dto;
    }
}
