package api.dieti2024.websocketTest;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping
public class controllerFaiOffertaWebSocket {

    @Autowired
    SimpMessagingTemplate simpleMessagingTemplate;

    @GetMapping("public/inviamsg/{astaid}")
    public String faiOfferta(@RequestParam String messaggio,@PathVariable String astaid) {
        String topic = "/asta/"+astaid;
        simpleMessagingTemplate.convertAndSend(topic, "un tizio ha detto: "+messaggio);
        return "qualcosa ho fatto poi sti cazzi";
    }

}
