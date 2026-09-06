package ru.itis.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRestrictionResponse {
    //UUID
    private String userId;

    private String blockType;

    private boolean blocked;

}
