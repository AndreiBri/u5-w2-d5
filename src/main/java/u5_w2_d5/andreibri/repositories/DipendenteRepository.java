package u5_w2_d5.andreibri.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import u5_w2_d5.andreibri.entities.Dipendente;

import java.util.Optional;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {

    Optional<Dipendente> findByUsername(String username);

    Optional<Dipendente> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
