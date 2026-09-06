package ru.itis.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponse {
    //UUID
    private String id;

    private String plasticName;
    //Наш contractName
    private String contractName;

    private String pan;

    private String expDate;

    private String cvv;

    private CardProduct cardProduct;

    private String userId;

}
