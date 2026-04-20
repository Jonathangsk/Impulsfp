package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.ApplicationUiModel
import com.impulsfp.mobile.network.ApiClient
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class ApplicationsController {

    private val api = ApiClient.applicationsApiService

    open suspend fun apply(
        offerId: String,
        sessionId: String
    ): Result<String> {
        return try {
            val response = api.applyToOffer(offerId, sessionId)

            println("APPLY APPLICATION offerId=$offerId")
            println("APPLY APPLICATION sessionId=$sessionId")
            println("APPLY APPLICATION code=${response.code()}")
            println("APPLY APPLICATION body=${response.body()}")
            println("APPLY APPLICATION errorBody=${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    400, 409 -> Result.failure(Exception("Ja estàs inscrit a aquesta oferta"))
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("Oferta no trobada"))
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

    open suspend fun getMyApplications(
        sessionId: String
    ): Result<List<ApplicationUiModel>> {
        return try {
            val response = api.getMyApplications(sessionId)

            if (response.isSuccessful) {
                val list = response.body()?.map {
                    ApplicationUiModel(
                        id = it.id,
                        offerTitle = it.offerTitle,
                        companyName = it.companyName,
                        location = it.location,
                        status = mapStatus(it.status),
                        appliedAt = it.appliedAt
                    )
                } ?: emptyList()

                Result.success(list)
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("No s'han trobat candidatures"))
                    500, 502, 503 -> Result.failure(Exception("El servidor no està disponible temporalment"))
                    else -> Result.failure(Exception("Error carregant candidatures"))
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

    private fun mapStatus(status: String): String {
        return when (status.uppercase()) {
            "PENDING" -> "Enviada"
            "ACCEPTED" -> "Acceptada"
            "REJECTED" -> "Rebutjada"
            else -> status
        }
    }
}