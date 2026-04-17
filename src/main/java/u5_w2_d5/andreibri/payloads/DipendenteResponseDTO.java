package u5_w2_d5.andreibri.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DipendenteResponseDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String password;
    private String email;
    private String avatar;
    private String username;
    private List<ViaggioResponseDTO> viaggi;


}
