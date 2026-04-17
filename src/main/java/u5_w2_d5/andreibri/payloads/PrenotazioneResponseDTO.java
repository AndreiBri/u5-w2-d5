package u5_w2_d5.andreibri.payloads;

import java.time.LocalDate;

public class PrenotazioneResponseDTO {

    private Long id;
    private ViaggioResponseDTO viaggo;
    private Long dipendenteId;
    private String dipendenteUsername;
    private LocalDate dataRichiesta;
    private String note;

}
