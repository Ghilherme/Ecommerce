package com.teamviewer.ecommerce.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessageApi {

    private HttpStatus status;
    private List<String> errors;
}
