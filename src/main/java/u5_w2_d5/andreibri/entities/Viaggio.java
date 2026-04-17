package u5_w2_d5.andreibri.entities;

import jakarta.persistence.*;
import lombok.*;
import u5_w2_d5.andreibri.enums.StatoViaggio;

import java.time.LocalDate;

@Entity
@Table(name = "viaggi")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Viaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinazione;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoViaggio stato;
}
