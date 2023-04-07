package com.asgardiateam.aptekaproject.payload;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    private String firstName;

    private String lastName;

    private String phone;

    private String login;
}
