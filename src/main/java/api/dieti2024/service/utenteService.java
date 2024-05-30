package api.dieti2024.service;

import api.dieti2024.dto.utente.ProfiloUtentePublicoDTO;
import api.dieti2024.dto.auth.UserDetailsDto;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Utente;
import api.dieti2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class utenteService {

    @Autowired
    private UserRepository utenteRepository;

    public UserDetailsDto getUserDetails(String email) {

        Utente utenteModel =getUtenteByEmail(email);
        return UserDetailsDto.fromUserModel(utenteModel);
    }

    /*
     * Metodo per ottenere un utente dal repository
     * @param email email dell'utente
     * @return utente
     * @throws ApiException se l'utente non è presente
     */
    private Utente getUtenteByEmail(String email) {
        Utente utente = utenteRepository.findById(email).orElseThrow(() -> new ApiException("Utente non trovato", HttpStatus.NOT_FOUND));
        return utente;
    }

    public ProfiloUtentePublicoDTO getDatiProfilo(String email){
        Utente utente = getUtenteByEmail(email);
        ProfiloUtentePublicoDTO profiloUtentePublicoDTO = ProfiloUtentePublicoDTO.fromUserModel(utente);
        return profiloUtentePublicoDTO;
    }
    public void updateDatiProfilo(String email,  ProfiloUtentePublicoDTO profiloUtentePublicoDTO){
        try {
            Utente utente = getUtenteByEmail(email);

            utente.setNome(profiloUtentePublicoDTO.nome());
            utente.setCognome(profiloUtentePublicoDTO.cognome());
            utente.setBio(profiloUtentePublicoDTO.bio());
            utente.setSiti(profiloUtentePublicoDTO.siti());
            utente.setAreaGeografica(profiloUtentePublicoDTO.areaGeografica());
            utenteRepository.save(utente);
        }catch (ApiException e) {
            throw e;
        }catch (Exception e){
            throw new ApiException("Errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
