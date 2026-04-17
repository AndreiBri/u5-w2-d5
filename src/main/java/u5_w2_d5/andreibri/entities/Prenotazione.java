package u5_w2_d5.andreibri.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "prenotazioni",
        // Un dipendente non puó avere piú prenotazioni per lo stesso giono
        uniqueConstraints = @UniqueConstraint(
                name = "uk_dipendente_data",
                columnNames = {"dipendente_id", "data_richiesta"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione Prenotazione -> Dipendente
    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    // Relazione Prenotazione -> Dipendente
    @ManyToOne
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;

    @Column(name = "data_richiesta", nullable = false)
    private LocalDate dataRichiesta;

    @Column(columnDefinition = "TEXT")
    private String note;
}
