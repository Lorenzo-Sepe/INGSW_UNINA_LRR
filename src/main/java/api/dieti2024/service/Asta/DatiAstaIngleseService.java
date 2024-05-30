package api.dieti2024.service.Asta;

import api.dieti2024.dto.asta.InputAstaDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.DatiAstaInglese;
import api.dieti2024.repository.DatiAstaIngleseRepository;
import api.dieti2024.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DatiAstaIngleseService {
    @Autowired
    DatiAstaIngleseRepository datiAstaIngleseRepository;

    public void salvaDatiAstaInglese(InputAstaDTO inputAstaDTO,int idAsta) {
        DatiAstaInglese datiAstaInglese = getDatiAstaInglese(inputAstaDTO, idAsta);
        datiAstaIngleseRepository.save(datiAstaInglese);
    }

    private static DatiAstaInglese getDatiAstaInglese(InputAstaDTO inputAstaDTO, int idAsta) {
        DatiAstaInglese datiAstaInglese;
        try{
            datiAstaInglese = JsonUtil.fromJson(inputAstaDTO.datiExtraJson(), DatiAstaInglese.class);
        }catch (Exception e){
            throw new ApiException("Dati asta inglese non validi", HttpStatus.BAD_REQUEST);
        }
        datiAstaInglese.setAstaId(idAsta);
        return datiAstaInglese;
    }
}
