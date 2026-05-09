package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.Offer
import com.impulsfp.mobile.network.ApiClient
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Classe encarregada de gestionar la comunicació amb el servidor
 * per a les operacions relacionades amb les ofertes.
 *
 * Aquesta classe permet obtenir el llistat d'ofertes disponibles
 * i inscriure l'usuari autenticat a una oferta concreta.
 *
 * També s'encarrega de tractar els possibles errors de connexió,
 * autenticació o resposta del servidor, retornant missatges descriptius
 * per facilitar la gestió des de la capa de presentació.
 *
 * @author abenitez
 */
open class OffersController {

    private val apiService = ApiClient.offersApiService

    /**
     * Recupera el llistat d'ofertes disponibles.
     *
     * Fa una petició al backend utilitzant l'identificador de sessió
     * i transforma la resposta en una llista d'objectes [Offer]
     * aptes per ser utilitzats dins l'aplicació.
     *
     * En cas que alguns camps opcionals no arribin informats, s'assignen
     * valors per defecte com cadenes buides, llistes buides o valors numèrics.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return [Result] amb:
     * - Llista de [Offer] si la consulta és correcta
     * - Excepció amb missatge descriptiu si es produeix algun error
     */
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
                            applicantsCount = offer.applicantsCount ?: 0,
                            cycle = offer.cycle ?: "",
                            testType = offer.testType,
                            testQuestion = offer.testQuestion,
                            codeSnippet = offer.codeSnippet,
                            options = offer.options
                                ?.split(";")
                                ?.map { it.trim() }
                                ?.filter { it.isNotBlank() }
                                ?: emptyList()
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
}