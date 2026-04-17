package u5_w2_d5.andreibri.payloads;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrenotazioneResponseDTO {

    private Long id;
    private ViaggioResponseDTO viaggio;
    private Long dipendenteId;
    private String dipendenteUsername;
    private LocalDate dataRichiesta;
    private String note;

}
