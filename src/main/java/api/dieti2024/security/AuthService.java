package api.dieti2024.security;

import api.dieti2024.dto.auth.CredenzialiUtenteDTO;
import api.dieti2024.dto.auth.DatiUtentePerTokenDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.model.Utente;
import api.dieti2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository utenteRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public  void saveTest() {
        Utente utente = new Utente();
        utente.setEmail("prova");
        utente.setPassword("prova");
        utente.setMetodoDiRegistrazione("prova");
        try {
            utenteRepo.save(utente);
        }catch (Exception e){
            throw new ApiException("Errore nel salvataggio", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public String registrazione(CredenzialiUtenteDTO credenzialiUtenteDTO) {
        try{
        //verifica se l'utente è già presente nel database
        if (utenteRepo.existsById(credenzialiUtenteDTO.email()) && credenzialiUtenteDTO.metodoDiRegistrazione().equals("dieti") ) {
            throw new ApiException("Utente già presente", HttpStatus.CONFLICT);
        }

        Utente utenteModel = new Utente();
        utenteModel.setEmail(credenzialiUtenteDTO.email());
        if(credenzialiUtenteDTO.metodoDiRegistrazione().equals("dieti")){
            utenteModel.setMetodoDiRegistrazione("dieti");
            utenteModel.setPassword(passwordEncoder.encode(credenzialiUtenteDTO.password()));
        }
        else{
            utenteModel.setMetodoDiRegistrazione("auth0");
        }

        utenteRepo.save(utenteModel);

        return jwtUtils.generateToken(DatiUtentePerTokenDTO.fromUserModel(utenteModel));
        }catch (ApiException e){
            throw e;
        }catch (Exception e){
            throw new ApiException("Errore nella registrazione", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    /**
     *
     * @param credenzialiUtenteDTO credenziali dell'utente
     * @return restituisce il token in caso di successo
     */
    public String login(final CredenzialiUtenteDTO credenzialiUtenteDTO){
        try {

            //se l'utente non è presente nel database lo registra
            if (!utenteRepo.existsById(credenzialiUtenteDTO.email()) && credenzialiUtenteDTO.metodoDiRegistrazione().equals("auth0") ) {
               return registrazione(credenzialiUtenteDTO);
            }
            Utente utenteRecuperatoTramiteEmail = VerificaUtente(credenzialiUtenteDTO);

            DatiUtentePerTokenDTO datiUtentePerTokenDTO = DatiUtentePerTokenDTO.fromUserModel(utenteRecuperatoTramiteEmail);
            return jwtUtils.generateToken(datiUtentePerTokenDTO);
        }catch(ApiException e){
            throw e;
        }catch (Exception e) {
            throw new ApiException("Utente non trovato", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Verifica l'utente abbia inserito le credenziali corrette
     * @param credenzialiUtenteDTO credenziali dell'utente
     * @return restituisce l'utente in caso di successo
     * @throws ApiException se le credenziali non sono corrette
     */
    private Utente VerificaUtente(CredenzialiUtenteDTO credenzialiUtenteDTO) {
        String email = credenzialiUtenteDTO.email();
        Utente utenteRecuperatoTramiteEmail= utenteRepo.findById(email).orElseThrow();

        if(credenzialiUtenteDTO.metodoDiRegistrazione().equals("dieti")){
            matchPassword(
                    credenzialiUtenteDTO.password(),
                    utenteRecuperatoTramiteEmail.getPassword()
            );
        }else{
            verificaTokenAuth0(credenzialiUtenteDTO, utenteRecuperatoTramiteEmail);
        }
        return utenteRecuperatoTramiteEmail;
    }

    private void verificaTokenAuth0(CredenzialiUtenteDTO credenzialiUtenteDTO, Utente utenteRecuperatoTramiteEmail) {
        String tokenDiAccessoAuth0 = credenzialiUtenteDTO.password();
        if(tokenDiAccessoAuth0.equals(utenteRecuperatoTramiteEmail.getPassword())){
            throw new ApiException("Token di accesso non valido", HttpStatus.UNAUTHORIZED);
        }
        /*TODO implementare la verifica del token di accesso di Auth0 in modo solido*/
    }


    /**
         * Verifica che la password ricevuta corrisponda a quella salvata nel database
         * @param passwordRicevuta password ricevuta
         * @param passworSalvataNelDatabase password salvata nel database
         * @throws ApiException se la password non corrisponde
         **/
    private void matchPassword(String passwordRicevuta, String passworSalvataNelDatabase) {
        if (!passwordEncoder.matches(passwordRicevuta, passworSalvataNelDatabase)){
                throw new ApiException("Password errata", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Codifica la password
     * @param passowordDaCodificare password da codificare
     * @return password codificata
     */
    private String codificaPassword(String passowordDaCodificare) {
        return  passwordEncoder.encode(passowordDaCodificare);
    }


}
