package u5_w2_d5.andreibri.payloads;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ViaggioResponseDTO {

    private Long id;
    private String destinazione;
    private LocalDate data;
    private String stato; // Restituito come stringa in modo leggibile
}
