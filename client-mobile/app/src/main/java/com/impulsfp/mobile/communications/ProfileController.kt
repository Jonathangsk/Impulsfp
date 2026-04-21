package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.UserProfile
import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.DeleteAccountRequest
import com.impulsfp.mobile.network.UpdateProfileRequest
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Classe encarregada de gestionar la comunicació amb el servidor
 * per a les operacions relacionades amb el perfil de l'usuari.
 *
 * Aquesta classe permet obtenir la informació del perfil, actualitzar
 * les dades personals i eliminar el compte de l'usuari autenticat.
 *
 * També s'encarrega de tractar els possibles errors de connexió,
 * autenticació o resposta del servidor, retornant missatges descriptius
 * per facilitar la gestió des de la capa de presentació.
 *
 * @author abenitez
 */
open class ProfileController {

    private val apiService = ApiClient.profileApiService

    /**
     * Recupera el perfil de l'usuari autenticat.
     *
     * Fa una petició al backend utilitzant l'identificador de sessió
     * i transforma la resposta en un objecte [UserProfile] apte per
     * ser utilitzat dins l'aplicació.
     *
     * En cas que alguns camps opcionals no arribin informats, s'assignen
     * valors per defecte com cadenes buides o llistes buides.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return [Result] amb:
     * - [UserProfile] si la consulta és correcta
     * - Excepció amb missatge descriptiu si es produeix algun error
     */
    open suspend fun getProfile(sessionId: String): Result<UserProfile> {
        return try {
            val response = apiService.getProfile(sessionId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val profile = UserProfile(
                        username = body.username,
                        name = body.name,
                        surname = body.surname,
                        email = body.email,
                        phoneNumber = body.phoneNumber ?: "",
                        city = body.city ?: "",
                        bio = body.bio ?: "",
                        cycle = body.cycle ?: "",
                        skills = body.skills ?: emptyList(),
                        experienceLevel = body.experienceLevel ?: "",
                        languages = body.languages ?: emptyList(),
                        preferredRoles = body.preferredRoles ?: emptyList(),
                        preferredLocation = body.preferredLocation ?: "",
                        availability = body.availability ?: "",
                        portfolio = body.portfolio ?: ""
                    )
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("Perfil no trobat"))
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
     * Actualitza les dades del perfil de l'usuari autenticat.
     *
     * Envia al servidor les noves dades del perfil associades a la sessió
     * activa i retorna el missatge confirmant el resultat de l'operació.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     * @param request Dades noves del perfil que s'han d'actualitzar
     *
     * @return [Result] amb:
     * - Missatge de confirmació si l'actualització és correcta
     * - Excepció amb missatge descriptiu si es produeix algun error
     */
    open suspend fun updateProfile(
        sessionId: String,
        request: UpdateProfileRequest
    ): Result<String> {
        return try {
            println("UPDATE PROFILE sessionId=$sessionId")
            println("UPDATE PROFILE request=$request")

            val response = apiService.updateProfile(sessionId, request)

            println("UPDATE PROFILE code=${response.code()}")
            println("UPDATE PROFILE body=${response.body()}")
            println("UPDATE PROFILE errorBody=${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    400 -> Result.failure(Exception("Aquest correu electrònic ja està en ús"))
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("Usuari no trobat"))
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
            e.printStackTrace()
            Result.failure(Exception("S'ha produït un error inesperat"))
        }
    }

    /**
     * Elimina el compte de l'usuari autenticat.
     *
     * Envia al servidor la sol·licitud d'eliminació del compte,
     * validant la sessió activa i la contrasenya proporcionada
     * per confirmar l'operació.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     * @param password Contrasenya introduïda per confirmar l'eliminació
     *
     * @return [Result] amb:
     * - Missatge de confirmació si el compte s'ha eliminat correctament
     * - Excepció amb missatge descriptiu si es produeix algun error
     */
    open suspend fun deleteAccount(
        sessionId: String,
        password: String
    ): Result<String> {
        return try {
            val response = apiService.deleteAccount(
                sessionId,
                DeleteAccountRequest(password)
            )

            if (response.isSuccessful) {
                val message = response.body()?.get("message")
                    ?: "Compte eliminat correctament"

                Result.success(message)
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("Usuari no trobat"))
                    400 -> Result.failure(Exception("Contrasenya incorrecta"))
                    else -> Result.failure(Exception("Error del servidor"))
                }
            }

        } catch (e: Exception) {
            Result.failure(Exception("No s'ha pogut eliminar el compte"))
        }
    }
}