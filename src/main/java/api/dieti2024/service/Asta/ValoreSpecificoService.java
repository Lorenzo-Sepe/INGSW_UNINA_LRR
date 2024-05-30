package api.dieti2024.service.Asta;

import api.dieti2024.model.ValoreSpecificoPerProdotto;
import api.dieti2024.repository.ValoreSpecificoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoreSpecificoService {
    @Autowired
    private ValoreSpecificoRepository valoreSpecificoRepository;


    public void saveAll(List<ValoreSpecificoPerProdotto> lista) {
        if (lista == null || lista.isEmpty()) {
            return;
        }
        for (ValoreSpecificoPerProdotto nodo : lista) {
            try {
                valoreSpecificoRepository.save(nodo);
            } catch (Exception e) {
                System.out.println("Errore nel salvataggio del valore specifico nodo: " + nodo.toString() + e.getMessage());
            }
        }
    }
}
