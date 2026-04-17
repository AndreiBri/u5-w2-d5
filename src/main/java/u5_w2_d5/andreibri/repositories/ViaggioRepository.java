package u5_w2_d5.andreibri.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import u5_w2_d5.andreibri.entities.Viaggio;

@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {
}
