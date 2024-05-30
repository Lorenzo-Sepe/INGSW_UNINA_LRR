package api.dieti2024.service.Asta;

import api.dieti2024.dto.asta.ricerca.FiltroDto;
import api.dieti2024.dto.asta.ricerca.InfoDatiAstaDTO;
import api.dieti2024.model.DatiAstaInglese;
import api.dieti2024.repository.*;
import api.dieti2024.util.TipoPermesso;
import api.dieti2024.dto.asta.InputAstaDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Asta;
import api.dieti2024.util.CalendarioUtil;
import api.dieti2024.util.ControllerRestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AstaService {

    @Autowired
    private AstaRepository astaRepository;

    @Autowired
    private PermessoRepository permessoRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private AstaProdottoRepository astaProdottoRepository;

    @Autowired
    private DatiAstaIngleseRepository datiAstaIngleseRepository;
    public void checkDatiInputi(InputAstaDTO inputAstaDTO) {

        if (inputAstaDTO == null) throw new ApiException("Dati asta mancanti", HttpStatus.BAD_REQUEST);
        if (!checkPermessoDiCreazione(inputAstaDTO.tipoAsta())) throw new ApiException("Permesso di creazione asta non valido", HttpStatus.FORBIDDEN);

        if (inputAstaDTO.baseAsta() <= 0) throw new ApiException("Base asta non valida", HttpStatus.BAD_REQUEST);

        long dataInizio = inputAstaDTO.dataInizio();
        long dataScadenza = inputAstaDTO.dataScadenza();
        long dataAttuale = CalendarioUtil.ottieniTempoAttuale();
        if (!(dataInizio>=dataAttuale && dataInizio<dataScadenza)) throw new ApiException("Tempo asta non valido", HttpStatus.BAD_REQUEST);


    }

    private boolean checkPermessoDiCreazione(String tipoAsta) {
        String emailUtente = ControllerRestUtil.getEmailOfUtenteCorrente();
        List<String> permessiUtente = permessoRepository.getPermessiUtente(emailUtente);
        return TipoPermesso.haPermessoDiCreazione(tipoAsta, permessiUtente) ;
    }

    public int salvaAsta (InputAstaDTO inputAstaDTO,int idProdotto){
        String emailUtenteCreatore = ControllerRestUtil.getEmailOfUtenteCorrente();
        Asta asta = inputAstaDTO.toAsta(idProdotto, emailUtenteCreatore);
        return astaRepository.save(asta).getId();
    }

    public List<InfoDatiAstaDTO> getAllAstaProdotto(FiltroDto filtroDto){
        List<InfoDatiAstaDTO> lista= astaProdottoRepository.findByCustomWhere(filtroDto, filtroDto.elementiPerPagina(), (filtroDto.pagina() - 1) * filtroDto.elementiPerPagina());

        return lista;
    }

    public int getNumeroTotaleDiAstePerRicerca(FiltroDto filtroDto){

        return astaProdottoRepository.numeroTotaleDiAstePerRicerca(filtroDto);
    }

    public Asta getAstaById(int i) {
        return astaRepository.findById(i).orElseThrow(() -> new ApiException("Asta non trovata", HttpStatus.NOT_FOUND));
    }

    public DatiAstaInglese getDatiAstaIngleseById(int id) {
        return datiAstaIngleseRepository.findById(id).orElseThrow(() -> new ApiException("Dati asta inglese non trovati", HttpStatus.NOT_FOUND));
    }
}

