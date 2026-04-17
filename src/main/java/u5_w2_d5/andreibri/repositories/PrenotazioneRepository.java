package u5_w2_d5.andreibri.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import u5_w2_d5.andreibri.entities.Prenotazione;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findByDipendenteId(Long dipendenteId);

    List<Prenotazione> findByViaggioId(Long viaggioId);

    // Controllare se il dipendente ha giá una prenotazione per quella data
    boolean existsByDipendenteIdAndDataRichiesta(Long dipendenteId, LocalDate dataRichiesta);

}
