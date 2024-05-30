package api.dieti2024.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessaggioDiErrore {
    private int CodiceDiErrore;
    private Date timestamp;
    private String messaggio;
    private String descrizione;

}