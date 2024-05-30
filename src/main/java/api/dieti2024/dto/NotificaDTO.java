package api.dieti2024.dto;

import lombok.Builder;

@Builder
public record NotificaDTO(String email, String messaggio) {

}
