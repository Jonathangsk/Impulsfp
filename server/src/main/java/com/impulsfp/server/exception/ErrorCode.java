package com.impulsfp.server.exception;


/**
 * Enum per representar els diferents codis d'error que poden ocórrer a l'API.
 *
 * @author Jonathan Giraldo Giraldo
 */
public enum ErrorCode {
    INVALID_USERNAME,
    INVALID_PASSWORD,
    USER_NOT_FOUND,
    USER_ALREADY_EXISTS,
    INVALID_SESSION,
    INVALID_EMAIL
}