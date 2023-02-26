package com.asgardiateam.aptekaproject.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtTokenDTO {

    private String accessToken;

    private Long expiresIn;

    private String tokenType;

}
