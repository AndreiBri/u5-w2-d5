package u5_w2_d5.andreibri.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneRequestDTO {

    @NotNull(message = "L'ID del viaggio é obbligatorio")
    private Long viaggioId;

    @NotNull(message = "L'ID del dipendente é obbligatorio")
    private Long dipendenteId;

    @NotNull(message = "La data di richiesta é obbligatoria")
    private LocalDate dataRichiesta;

    @Size(max = 500, message = "Le note devono avere un massimo di 500 caratteri")
    private String note;
}
