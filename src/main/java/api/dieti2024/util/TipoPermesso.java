package api.dieti2024.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipoPermesso {

        public static class Crea {
            public static final String ASTAINGLESE = "crea_asta_inglese";
            public static final String APPALTO = "crea_asta_inversa";
            public static final String  SILENZIOSA = "crea_asta_silenziosa";
        }

        public static class FaiOfferta {
            public static final String ASTAINGLESE = "fai_offerta_asta_inglese";
            public static final String APPALTO = "fai_offerta_asta_inversa";
            public static final String SILENZIOSA = "fai_offerta_asta_silenziosa";
        }

    private static final Map<String, String> mappaPermessiCreazione = new HashMap<>() {
        {
            put(TipoAsta.INGLESE, TipoPermesso.Crea.ASTAINGLESE);
            put(TipoAsta.SILENZIOSA, TipoPermesso.Crea.SILENZIOSA);
            put(TipoAsta.APPALTO, TipoPermesso.Crea.APPALTO);
        }
    };
    private static final Map<String, String> mappaPermessiOfferta = new HashMap<>(){
        {
            put(TipoAsta.INGLESE, TipoPermesso.FaiOfferta.ASTAINGLESE);
            put(TipoAsta.SILENZIOSA, TipoPermesso.FaiOfferta.SILENZIOSA);
            put(TipoAsta.APPALTO, TipoPermesso.FaiOfferta.APPALTO);
        }
    };



    public static boolean haPermessoDiCreazione(String tipoAsta, List<String> permessiUtente) {
        String permessoCorrispondente = mappaPermessiCreazione.get(tipoAsta);
        return permessoCorrispondente != null && permessiUtente.contains(permessoCorrispondente);
    }

    public static boolean haPermessoDiOfferta(String tipoAsta, List<String> permessiUtente) {
        String permessoCorrispondente = mappaPermessiOfferta.get(tipoAsta);
        return permessoCorrispondente != null && permessiUtente.contains(permessoCorrispondente);
    }


}



