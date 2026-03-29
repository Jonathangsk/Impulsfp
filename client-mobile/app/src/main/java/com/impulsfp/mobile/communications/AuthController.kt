package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.User
import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.LoginRequest
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Classe encarregada de gestionar la comunicació amb el servidor
 * per a les operacions d'autenticació.
 *
 * Aquesta classe envia les peticions de login i logout al backend
 * i transforma les respostes del servidor en objectes útils per a l'app.
 *
 * @abenitez
 */
open class AuthController {

    private val apiService = ApiClient.authApiService

    /**
     * Realitza el login contra el servidor.
     *
     * @param username Nom d'usuari introduït a la pantalla del login
     * @param password Contrasenya introduïda a la pantalla del login
     *
     * @return Result<User>
     * - Si tot va bé retorna un User
     * - Si hi ha error, retorna una excepció amb un missatge explicatiu
     */
    open suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = apiService.login(
                LoginRequest(
                    username = username,
                    password = password
                )
            )

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val user = User(
                        username = username,
                        role = mapUserType(body.userType),
                        sessionId = body.sessionId
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Usuari o contrasenya incorrectes"))
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
     * Realitza el logout contra el servidor utilitzant l'identificador de sessió.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return Resultat de l'operació de logout:
     * - Si és correcte, retorna [Unit]
     * - Si hi ha error, retorna una excepció amb un missatge explicatiu
     */
    open suspend fun logout(sessionId: String): Result<Unit> {
        return try {
            val response = apiService.logout(sessionId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
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
     * Converteix el tipus d'usuari retornat pel backend al rol intern
     * utilitzat per la interfície de l'app.
     *
     * @param userType Valor rebut del servidor:
     * - student
     * - company
     * - admin
     *
     * @return Rol intern equivalent:
     * - ALUMNE
     * - EMPRESA
     * - ADMIN
     * - DESCONEGUT en cas de valor no reconegut
     */
    private fun mapUserType(userType: String): String {
        return when (userType.lowercase()) {
            "student" -> "ALUMNE"
            "company" -> "EMPRESA"
            "admin" -> "ADMIN"
            else -> "DESCONEGUT"
        }
    }
}