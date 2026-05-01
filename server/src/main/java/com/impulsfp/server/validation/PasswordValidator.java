package com.impulsfp.server.validation;

import com.impulsfp.server.exception.ApiException;
import com.impulsfp.server.exception.ErrorCode;

/**
 * Classe utilitària per validar la contrasenya d'un usuari segons criteris específics; assegura que la contrasenya compleixi amb els requisits de seguretat establerts.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class PasswordValidator {

    private static final String REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[a-z]).{6,}$";

    public static void validate(String password){
        if (password == null || !password.matches(REGEX)) {
            throw new ApiException(
                    ErrorCode.INVALID_PASSWORD,
                    "La contrasenya no és vàlida; > 6 caracteres, mínim una majúscula, una minúscula i un número."
            );
        }
    }
}