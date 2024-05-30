package api.dieti2024.dto.utente;

import api.dieti2024.model.Utente;

public record ProfiloUtentePublicoDTO(String nome, String cognome, String siti, String areaGeografica, String bio) {
    private ProfiloUtentePublicoDTO(Utente utenteModel) {
        this(utenteModel.getNome(), utenteModel.getCognome(), utenteModel.getSiti(), utenteModel.getAreaGeografica(), utenteModel.getBio());
    }

    public static ProfiloUtentePublicoDTO fromUserModel(Utente utenteModel) {
        return new ProfiloUtentePublicoDTO(utenteModel);
    }
}
