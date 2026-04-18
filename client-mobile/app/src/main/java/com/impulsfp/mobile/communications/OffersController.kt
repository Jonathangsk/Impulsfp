package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.Offer
import com.impulsfp.mobile.network.ApiClient
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class OffersController {

    private val apiService = ApiClient.offersApiService

    open suspend fun getOffers(sessionId: String): Result<List<Offer>> {
        return try {
            val response = apiService.getOffers(sessionId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val offers = body.map { offer ->
                        Offer(
                            id = offer.id.toString(),
                            title = offer.title,
                            description = offer.description,
                            company = offer.companyName,
                            requiredSkills = offer.skills,
                            location = offer.location,
                            modality = offer.modality,
                            contractType = offer.contractType,
                            salary = offer.salary?.toString(),
                            createdAt = offer.creationDate,
                            state = offer.state,
                            applicantsCount = offer.applicantsCount ?: 0
                        )
                    }

                    Result.success(offers)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("No s'han trobat ofertes"))
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

    open suspend fun applyToOffer(
        offerId: String,
        sessionId: String
    ): Result<String> {
        return try {
            val response = apiService.applyToOffer(offerId, sessionId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    400 -> Result.failure(Exception("Ja t'has inscrit a aquesta oferta"))
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
}