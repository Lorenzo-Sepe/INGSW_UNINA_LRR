package api.dieti2024.controller;

import api.dieti2024.dto.NotificaDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class NotificheController {

    @MessageMapping("/notifica")
    @SendTo("/topic/notifica")
    public NotificaDTO inviaNotifica(@Payload NotificaDTO notificaDTO) {
        return notificaDTO;
    }

    public NotificaDTO addUtente(@Payload NotificaDTO notificaDTO, SimpMessageHeaderAccessor headerAccessor) {
        // aggiungo l'utente alla sessione tramite l'email per poterlo identificare
        headerAccessor.getSessionAttributes().put("email", notificaDTO.email());
        return notificaDTO;
    }
}
