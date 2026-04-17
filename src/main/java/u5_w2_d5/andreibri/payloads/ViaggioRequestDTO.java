package u5_w2_d5.andreibri.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ViaggioRequestDTO {

    @NotBlank(message = "La destinazione é obbligatoria")
    private String destinazione;

    @NotNull(message = "La data é obbligatoria")
    private LocalDate data;

    // Stato come String nel DTO (da convertire in Enum nel Service)
    @NotBlank(message = "Lo stato é obbligatorio")
    private String stato; // IN_PROGRAMMA o COMPLETATO
}
