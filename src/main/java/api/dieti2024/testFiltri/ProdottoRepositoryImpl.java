package api.dieti2024.testFiltri;

import api.dieti2024.dto.asta.ricerca.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProdottoRepositoryImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List< InfoDatiAstaDTO> getProdottiAstaConFiltroCompleto(FiltroDto filtroDto) {
        //init array without element to avoid null pointer exception
        List<Object> params = new ArrayList<>();
        String query = getQueryPerRicercaConFiltri(filtroDto, params);
        // Creazione array di oggetti contenenti ID e valori
           Object[] paramsArray =params.toArray();
        return jdbcTemplate.query(query, paramsArray,(rs, rowNum) -> {
            InfoDatiAstaDTO infoDatiAstaDTO = new InfoDatiAstaDTO();
            infoDatiAstaDTO.setIdAsta(rs.getInt("id"));
            infoDatiAstaDTO.setIdAsta(rs.getInt("id_asta"));
            infoDatiAstaDTO.setBaseAsta(rs.getDouble("base_asta"));
            infoDatiAstaDTO.setPrezzoAttuale(rs.getDouble("prezzo_attuale"));
            infoDatiAstaDTO.setDataScadenza(rs.getLong("data_scadenza"));
            infoDatiAstaDTO.setDataInizio(rs.getLong("data_inizio"));
            infoDatiAstaDTO.setTipoAsta(rs.getString("tipo"));
            infoDatiAstaDTO.setEmailUtenteCreatore(rs.getString("utente_creatore"));
            infoDatiAstaDTO.setNome(rs.getString("nome_prodotto"));
            String listaImgString = rs.getString("lista_img");
            if (listaImgString != null && !listaImgString.isEmpty()) {
                List<String> listaDiImmagini = Arrays.asList(listaImgString.split(","));
                infoDatiAstaDTO.setImmagini(listaDiImmagini);
            } else {
                infoDatiAstaDTO.setImmagini(List.of("NULL","NULL") ) ;
            }
            infoDatiAstaDTO.setDescrizione(rs.getString("descrizione"));
            infoDatiAstaDTO.setCategoria(rs.getString("categoria"));
            return infoDatiAstaDTO;
        });
        }

    private String getQueryPerRicercaConFiltri(FiltroDto filtroDto, List<Object> params) {
        String query = getQueryBasePerRicercaConFiltri(filtroDto, params,false);
        return query;
    }

    private String getQueryBasePerRicercaConFiltri(FiltroDto filtroDto, List<Object> params,Boolean isCount) {
        String where = makeWhere(filtroDto, params);
        if (isCount) {
            return "SELECT COUNT(*) FROM asta_join_prodotto p " + where ;
        }
        String orderBy = makeOrderBy(filtroDto.campoOrdinamento(), filtroDto.direzioneOrdinamento());
        String query= "SELECT p.* ,string_agg(i.immagini,', ') as lista_img   FROM asta_join_prodotto p " +
                " LEFT JOIN valore_specifico_di_un_prodotto v ON p.id = v.id_prodotto  LEFT JOIN prodotto_immagini i ON i.prodotto_id=p.id" +
                where +
                " group by p.id, id_asta, base_asta, prezzo_attuale, data_scadenza, data_inizio, tipo, utente_creatore, nome_prodotto, p.immagini, descrizione, categoria " +
                orderBy ;

        query = addQueryLimiteAndOffset(filtroDto, params, query);
        return query;
    }

    private String makeOrderBy(CampoOrdinamento campoOrdinamento, DirezioneOrdinamento direzioneOrdinamento) {
        if (campoOrdinamento == null || direzioneOrdinamento == null) {
            return " ";
        }
        return " ORDER BY " + campoOrdinamento.getCampo() + " " + direzioneOrdinamento.getDirezione();
    }

    private static String addQueryLimiteAndOffset(FiltroDto filtroDto, List<Object> params, String query) {
        query +=        " LIMIT ? OFFSET ?" ;
        params.add(filtroDto.elementiPerPagina());
        int offset = (filtroDto.pagina() - 1) * filtroDto.elementiPerPagina();
        params.add(offset);
        return query;
    }





    private String makeWhere( FiltroDto filtroDto,List<Object> params) {

        boolean isEmpty= true;
        StringBuilder stringBuilder = new StringBuilder();

    if (filtroDto.prezzoMax()!=null || filtroDto.prezzoMin()!=null) {
        isEmpty = false;
        if (filtroDto.prezzoMin()!=null && !filtroDto.prezzoMin().isNaN()) {
            stringBuilder.append("prezzo_attuale >= ? AND  ");
            params.add(filtroDto.prezzoMin());
        }
        if (filtroDto.prezzoMax()!=null && !filtroDto.prezzoMax().isNaN()) {
            stringBuilder.append("prezzo_attuale <= ? AND  ");
            params.add(filtroDto.prezzoMax());
        }
    }

        if (filtroDto.caratteristicheSelezionate()!=null &&!filtroDto.caratteristicheSelezionate().isEmpty()) {
        String clausolaWherePerValoriSpecifici = getClausolaWherePerValoriSpecifici(
                filtroDto.caratteristicheSelezionate(),
                params);
        stringBuilder.append(clausolaWherePerValoriSpecifici);
        isEmpty= false;
    }

        // Aggiungi condizioni per categoria, tipoAsta e nomeProdotto solo se non sono
        // nulli o vuoti
        if (filtroDto.categoria() != null && !filtroDto.categoria().isEmpty() && !filtroDto.categoria().equals("tutte")) {
            stringBuilder.append(" categoria In (SELECT * FROM TrovaFigliCategoria(?) ) AND  ");
            params.add(filtroDto.categoria());
            isEmpty= false;
        }
        // Aggiungi condizioni per tipoAsta solo se non è vuoto
        if (filtroDto.tipoAsta()!=null &&!filtroDto.tipoAsta().isEmpty()) {
            String puntiIterogativi = createQuestionMarks(filtroDto.tipoAsta());
            stringBuilder.append("tipo IN ("+puntiIterogativi+") AND  ");
            params.addAll(filtroDto.tipoAsta());
            isEmpty= false;
        }

        if (filtroDto.nomeProdotto() != null && !filtroDto.nomeProdotto().isEmpty()) {
            stringBuilder.append("nome_prodotto ILIKE ? AND  ");
            params.add("%"+filtroDto.nomeProdotto()+"%");
            isEmpty= false;
        }

        // Trova l'ultima occorrenza di "AND" e rimuovi l'eventuale "AND" finale
        int lastIndex = stringBuilder.lastIndexOf("AND");
        if (lastIndex != -1) {
            stringBuilder.delete(lastIndex, stringBuilder.length());
            isEmpty= false;
        }
        if(isEmpty) return "";
        return " WHERE " +stringBuilder.toString()+"   ";
    }

    private String getClausolaWherePerValoriSpecifici( List<FiltroCaratteristicheDTO> filtroCaratteristicheDTOS, List<Object> params) {
        if (filtroCaratteristicheDTOS==null && filtroCaratteristicheDTOS.isEmpty()) {
            return "";
        }
        String where = " p.id IN ( ";
        //foreach filtroCaratteristicheDTOS
        int count = 0;
        for(FiltroCaratteristicheDTO caratteristica : filtroCaratteristicheDTOS){
            count++;
            where += " select id_prodotto from valore_specifico_di_un_prodotto v2 where  id_caratteristica = ? and valore in ( ";
            // ?, ?, ? ... FOR EACH ELEMENT IN caratteristiche.get(i)
            where += createQuestionMarks(caratteristica.valoriSelezionati());
            where += " )     ";
            params.add(caratteristica.idCaratteristica());
            params.addAll(caratteristica.valoriSelezionati());
            if (count < filtroCaratteristicheDTOS.size()) {
                where += " INTERSECT  ";
            }

        }
        where += ") AND  ";
        return where;
    }

    public String createQuestionMarks(List<String> list) {
        List<String> questionMarks = Collections.nCopies(list.size(), "?");
        return String.join(", ", questionMarks);
    }


    public int getNumeroProdottiAstaConFiltroCompleto(FiltroDto filtroDto) {
        //init array without element to avoid null pointer exception
        List<Object> params = new ArrayList<>();
        String query = getQueryBasePerRicercaConFiltri(filtroDto, params,true);
        // Creazione array di oggetti contenenti ID e valori
        Object[] paramsArray =params.toArray();
        //return jdbcTemplate.queryForObject(query, paramsArray, Integer.class);
        return jdbcTemplate.queryForObject(query, paramsArray, Integer.class);

    }
}