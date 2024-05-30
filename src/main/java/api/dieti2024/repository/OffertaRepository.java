package api.dieti2024.repository;

import api.dieti2024.model.Offerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OffertaRepository extends JpaRepository<Offerta, Long>{

}
