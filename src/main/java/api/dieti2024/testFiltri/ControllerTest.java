package api.dieti2024.testFiltri;


import api.dieti2024.dto.asta.ricerca.FiltroDto;
import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import api.dieti2024.repository.AstaProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ControllerTest {

    @Autowired
    AstaProdottoRepository astaProdottoRepository;

    @PostMapping("public/testfiltro")
    public Object searchProducts(@RequestBody List<CoppiaValoriTest> caratteristiche) {
        CaratteristicheTest caratteristicheWrapper = new CaratteristicheTest(caratteristiche);
        return astaProdottoRepository.searchProductsWithSpecificValues(caratteristicheWrapper);
    }

    @Autowired
    ProdottoRepositoryImpl prodottoRepository;
    @PostMapping("public/asta/getAllAste")
    public List< InfoDatiAstaDTO>   lanciaQuery(@RequestBody FiltroDto filtroDto){
        String query= "Select * from prodotto where nome_prodotto LIKE '%Rai%'";

        List<InfoDatiAstaDTO> obj= prodottoRepository.getProdottiAstaConFiltroCompleto( filtroDto);
        return obj;
    }
    //public/asta/getNumeroAste
    @PostMapping("public/asta/getNumeroAste")
    public int getNumeroAste(@RequestBody FiltroDto filtroDto){
        return prodottoRepository.getNumeroProdottiAstaConFiltroCompleto( filtroDto);
    }
}
