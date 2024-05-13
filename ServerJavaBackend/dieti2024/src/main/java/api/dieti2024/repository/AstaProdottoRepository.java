package api.dieti2024.repository;

import api.dieti2024.dto.asta.ricerca.FiltroDto;
import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AstaProdottoRepository extends JpaRepository<InfoDatiAstaDTO,Integer> {

    @Query(value = "SELECT * FROM asta_join_prodotto " +
            "WHERE (:#{#filtro.tipoAsta().isEmpty()} OR tipo = ANY( :#{#filtro.tipoAsta().toArray(new String[0])} )) " +
            "AND (:#{#filtro.categoria().equals('tutte')} OR  categoria= :#{#filtro.categoria}) " +
            "AND (:#{#filtro.nomeProdotto()} IS NULL OR nome_prodotto LIKE CONCAT('%', :#{#filtro.nomeProdotto()}, '%')) " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<InfoDatiAstaDTO> findByCustomWhere(FiltroDto filtro, int limit, int offset);

    @Query(value = "SELECT COUNT(*) FROM asta_join_prodotto " +
            "WHERE (:#{#filtro.tipoAsta().isEmpty()} OR tipo = ANY( :#{#filtro.tipoAsta().toArray(new String[0])} )) " +
            "AND (:#{#filtro.categoria().equals('tutte')} OR categoria = :#{#filtro.categoria}) " +
            "AND (:#{#filtro.nomeProdotto()} IS NULL OR nome_prodotto LIKE CONCAT('%', :#{#filtro.nomeProdotto()}, '%')) ",nativeQuery = true)
    int numeroTotaleDiAstePerRicerca(FiltroDto filtro);


}
