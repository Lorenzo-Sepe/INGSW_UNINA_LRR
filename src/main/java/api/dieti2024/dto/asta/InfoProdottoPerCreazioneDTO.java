package api.dieti2024.dto.asta;

import api.dieti2024.model.Prodotto;
import api.dieti2024.model.ValoreSpecificoPerProdotto;

import java.util.ArrayList;
import java.util.List;

public record InfoProdottoPerCreazioneDTO(
        String nomeProdotto,
        String descrizioneProdotto,
        List<String> immagini,
        String categoriaProdotto,
        List<CoppiaCaratteristicaValoreDTO> caratteristicheProdotto) {
    public Prodotto toProdotto() {
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(nomeProdotto);
        prodotto.setDescrizione(descrizioneProdotto);
        prodotto.setImmagini(immagini);
        prodotto.setCategoria(categoriaProdotto);
        return prodotto;
    }

    public List<ValoreSpecificoPerProdotto> toListValoreSpecificoPerProdotto(int idProdotto) {
        ArrayList<ValoreSpecificoPerProdotto> lista = new ArrayList<ValoreSpecificoPerProdotto>();
        for (CoppiaCaratteristicaValoreDTO coppia : caratteristicheProdotto) {
            ValoreSpecificoPerProdotto valore = new ValoreSpecificoPerProdotto();
            valore.setIdProdotto(idProdotto);
            valore.setIdCaratteristica(coppia.idCaratteristica());
            valore.setValore(coppia.valore());
            lista.add(valore);
        }
        return lista;
    }
}
