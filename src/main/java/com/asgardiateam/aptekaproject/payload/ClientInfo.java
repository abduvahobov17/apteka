package com.asgardiateam.aptekaproject.payload;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfo {

    private String firstName;

    private String lastName;

    private String phoneNumber;

}
