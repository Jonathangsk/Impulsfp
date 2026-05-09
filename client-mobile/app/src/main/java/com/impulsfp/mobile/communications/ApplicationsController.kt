package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.ApplicationUiModel
import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.ApplyRequest
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Classe encarregada de gestionar la comunicació amb el servidor
 * per a les operacions relacionades amb les candidatures de l'usuari.
 *
 * Aquesta classe permet inscriure l'usuari autenticat a una oferta
 * i recuperar el llistat de candidatures enviades.
 *
 * També s'encarrega de tractar els possibles errors de connexió,
 * autenticació o resposta del servidor, retornant missatges descriptius
 * per facilitar la gestió des de la capa de presentació.
 *
 * @author abenitez
 */
open class ApplicationsController {

    private val api = ApiClient.applicationsApiService

    /**
     * Inscriu l'usuari autenticat a una oferta.
     *
     * Envia al servidor la sol·licitud de candidatura associada
     * a l'oferta indicada, utilitzant l'identificador de sessió
     * de l'usuari autenticat.
     *
     * Opcionalment, permet enviar una resposta o missatge associat
     * a la candidatura.
     *
     * @param offerId Identificador de l'oferta a la qual es vol inscriure l'usuari
     * @param sessionId Identificador de sessió de l'usuari autenticat
     * @param answer Resposta opcional enviada juntament amb la candidatura
     *
     * @return [Result] amb:
     * - Missatge de confirmació si la inscripció és correcta
     * - Excepció amb missatge descriptiu si es produeix algun error
     */
    open suspend fun apply(
        offerId: String,
        sessionId: String,
        answer: String? = null
    ): Result<String> {
        return try {
            val response = api.applyToOffer(
                sessionId = sessionId,
                request = ApplyRequest(
                    offerId = offerId,
                    answer = answer
                )
            )

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

    /**
     * Recupera les candidatures de l'usuari autenticat.
     *
     * Fa una petició al backend utilitzant l'identificador de sessió
     * i transforma la resposta en una llista d'objectes [ApplicationUiModel]
     * aptes per ser mostrats a la interfície de l'aplicació.
     *
     * També converteix l'estat intern de la candidatura rebut del servidor
     * en un text més entenedor per a l'usuari.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return [Result] amb:
     * - Llista de [ApplicationUiModel] si la consulta és correcta
     * - Excepció amb missatge descriptiu si es produeix algun error
     */
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

    /**
     * Converteix l'estat intern d'una candidatura en un text llegible.
     *
     * Rep l'estat retornat pel servidor i el transforma en una etiqueta
     * més clara per mostrar-la a la interfície d'usuari.
     *
     * @param status Estat original de la candidatura retornat pel servidor
     *
     * @return Text descriptiu de l'estat de la candidatura
     */
    private fun mapStatus(status: String): String {
        return when (status.uppercase()) {
            "PENDING" -> "Enviada"
            "ACCEPTED" -> "Acceptada"
            "REJECTED" -> "Rebutjada"
            else -> status
        }
    }
}