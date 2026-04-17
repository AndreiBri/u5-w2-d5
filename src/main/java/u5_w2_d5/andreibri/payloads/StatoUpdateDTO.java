package u5_w2_d5.andreibri.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StatoUpdateDTO {

    @NotBlank(message = "Il nuovo stato é obbligatorio")
    private String stato; // IN_PROGRAMMA o COMPLETATO
}
