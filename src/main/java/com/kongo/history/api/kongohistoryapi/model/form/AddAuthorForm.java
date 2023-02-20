package com.kongo.history.api.kongohistoryapi.model.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class AddAuthorForm implements Serializable {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String dateOfBirth;
    @NotBlank
    private String address;
    @NotBlank
    private String phoneNumber;
}
