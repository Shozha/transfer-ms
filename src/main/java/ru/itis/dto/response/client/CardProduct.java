package ru.itis.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardProduct {
    //UUID
    private String id;

    private String cardName;

    private String description;

    private String cardImageLink;

}
