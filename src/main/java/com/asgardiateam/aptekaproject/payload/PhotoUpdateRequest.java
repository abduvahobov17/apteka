package com.asgardiateam.aptekaproject.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

import static com.asgardiateam.aptekaproject.constants.MessageKey.PHOTO_NOT_VALID;
import static com.asgardiateam.aptekaproject.constants.MessageKey.TITLE_NOT_VALID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoUpdateRequest {

    @NotNull(message = TITLE_NOT_VALID)
    private String title;

    @NotNull(message = PHOTO_NOT_VALID)
    private MultipartFile file;

}
