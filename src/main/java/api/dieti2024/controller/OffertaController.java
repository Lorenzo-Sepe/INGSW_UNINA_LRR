package api.dieti2024.controller;

import api.dieti2024.dto.OffertaDto;
import api.dieti2024.dto.OffertaVincenteDto;
import api.dieti2024.model.Offerta;
import api.dieti2024.service.OffertaService;
import api.dieti2024.util.CalendarioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class OffertaController {

    final SimpMessagingTemplate simpleMessagingTemplate;

    final OffertaService offertaService;

    @Autowired
    public OffertaController(SimpMessagingTemplate simpleMessagingTemplate, OffertaService offertaService) {
        this.simpleMessagingTemplate = simpleMessagingTemplate;
        this.offertaService = offertaService;
    }

    @PostMapping("/faiOfferta")
    public Offerta faiOfferta(@RequestBody OffertaDto offertaDto) {

        long tempoOfferta = CalendarioUtil.ottieniTempoAttuale();
            Offerta offerta = offertaService.faiOfferta(offertaDto, tempoOfferta ) ;
            simpleMessagingTemplate.convertAndSend("/asta/"+offertaDto.idAsta(), "Un utente ha fatto un'offerta:\n"+offerta);
            return offerta;
    }

   @PostMapping("ConfermaOffertaVincente")
    public Offerta confermaOffertaVincente(@RequestBody OffertaVincenteDto offertaDto) {
        return offertaService.confermaOffertaVincente(offertaDto);
    }

}
