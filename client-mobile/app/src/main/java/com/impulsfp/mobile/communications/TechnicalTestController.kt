package com.impulsfp.mobile.communications

import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.SubmitTechnicalTestRequest
import com.impulsfp.mobile.network.TechnicalTestResponse
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class TechnicalTestController {

    private val apiService = ApiClient.offersApiService

    /*
    TEA4 - Proves tècniques

    Recupera una prova tècnica concreta a partir del seu identificador.

    open suspend fun getTechnicalTest(
        testId: String,
        sessionId: String
    ): Result<TechnicalTestResponse> {
        return try {
            val response = apiService.getTechnicalTest(testId, sessionId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("Prova tècnica no trobada"))
                    500, 502, 503 -> Result.failure(Exception("El servidor no està disponible temporalment"))
                    else -> Result.failure(Exception("Error del servidor: ${response.code()}"))
                }
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("No s'ha pogut localitzar el servidor"))
        } catch (e: ConnectException) {
            Result.failure(Exception("No s'ha pogut connectar amb el servidor"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Temps d'espera esgotat en connectar amb el servidor"))
        } catch (e: IOException) {
            Result.failure(Exception("Error de xarxa en connectar amb el servidor"))
        } catch (e: Exception) {
            Result.failure(Exception("S'ha produït un error inesperat"))
        }
    }

    Envia la resposta seleccionada per l'alumne a la prova tècnica.

    Si el temps ha expirat, es pot enviar la resposta buida i indicar
    timeExpired = true perquè el backend la marqui com a no superada.

    open suspend fun submitTechnicalTest(
        offerId: String,
        sessionId: String,
        technicalTestId: String,
        selectedAnswer: String,
        timeExpired: Boolean = false
    ): Result<String> {
        return try {
            val response = apiService.submitTechnicalTest(
                offerId = offerId,
                sessionId = sessionId,
                request = SubmitTechnicalTestRequest(
                    technicalTestId = technicalTestId,
                    selectedAnswer = selectedAnswer,
                    timeExpired = timeExpired
                )
            )

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    400 -> Result.failure(Exception("Has de seleccionar una resposta"))
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("Oferta o prova tècnica no trobada"))
                    500, 502, 503 -> Result.failure(Exception("El servidor no està disponible temporalment"))
                    else -> Result.failure(Exception("Error del servidor: ${response.code()}"))
                }
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("No s'ha pogut localitzar el servidor"))
        } catch (e: ConnectException) {
            Result.failure(Exception("No s'ha pogut connectar amb el servidor"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Temps d'espera esgotat en connectar amb el servidor"))
        } catch (e: IOException) {
            Result.failure(Exception("Error de xarxa en connectar amb el servidor"))
        } catch (e: Exception) {
            Result.failure(Exception("S'ha produït un error inesperat"))
        }
    }
    */
}