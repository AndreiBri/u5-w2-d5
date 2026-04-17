package u5_w2_d5.andreibri.payloads;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DipendenteRequestDTO {

    @NotBlank(message = "Lo username è obbligatorio")
    private String username;

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    private String cognome;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotNull
    @Size(min = 8, max = 15, message = "La password deve essere lunga minimo 8 caratteri e massimo 15")
    @NotBlank(message = "La password é obbligatoria")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "La password deve contenere almeno una maiuscola, una minuscola, numeri e caratteri speciali ")
    private String password;
}
