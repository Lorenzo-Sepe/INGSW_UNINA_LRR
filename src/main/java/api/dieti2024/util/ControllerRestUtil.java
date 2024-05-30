package api.dieti2024.util;

import api.dieti2024.dto.auth.UserDetailsDto;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class ControllerRestUtil {

    /**
     * Metodo per ottenere l'email dell'utente tramite il SecurityContext
     *
     * @return email dell'utente
     */
    public static String getEmailOfUtenteCorrente() {
        UserDetailsDto dettagliUser = (UserDetailsDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return dettagliUser.email();
    }

    /**
     * Metodo per ottenere i permessi dell'utente corrente tramite il SecurityContext
     * @return lista di permessi dell'utente
     */
    public static List<String> getPermessiOfUtenteCorrente() {
        List<String> permessi= (List<String>) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return permessi;
    }

    public static  boolean hasPermeessoDiFareUnOfferta(String tipoAsta){
        List<String> permessiUtente = getPermessiOfUtenteCorrente();
        return TipoPermesso.haPermessoDiOfferta(tipoAsta,permessiUtente);
    }
    public static boolean hasPermsssoDiCreareUnAsta(String tipoAsta){
        List<String> permessiUtente = getPermessiOfUtenteCorrente();
        return TipoPermesso.haPermessoDiCreazione(tipoAsta,permessiUtente);
    }

}
