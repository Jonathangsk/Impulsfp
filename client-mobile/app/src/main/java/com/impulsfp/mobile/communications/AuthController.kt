package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.User
import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.LoginRequest
import com.impulsfp.mobile.network.RegisterRequest
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Classe encarregada de gestionar la comunicació amb el servidor
 * per a les operacions d'autenticació i registre d'usuaris.
 *
 * Aquesta classe centralitza les peticions relacionades amb la sessió
 * d'usuari, com ara el login, logout i registre d'alumnes, transformant
 * les respostes del backend en objectes útils per a l'aplicació.
 *
 * També s'encarrega de controlar els possibles errors de connexió,
 * servidor o validació, retornant missatges descriptius.
 *
 * @author abenitez
 */
open class AuthController {

    private val apiService = ApiClient.authApiService

    /**
     * Realitza el login contra el servidor.
     *
     * Envia les credencials introduïdes per l'usuari al backend i,
     * si l'autenticació és correcta, construeix un objecte [User]
     * amb les dades retornades.
     *
     * @param username Nom d'usuari introduït a la pantalla de login
     * @param password Contrasenya introduïda per l'usuari
     *
     * @return [Result] amb:
     * - [User] si l'autenticació és correcta
     * - Excepció amb missatge descriptiu si hi ha error
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
            e.printStackTrace()
            Result.failure(Exception("Temps d'espera esgotat en connectar amb el servidor"))
        } catch (e: javax.net.ssl.SSLHandshakeException) {
            e.printStackTrace()
            Result.failure(Exception("Error SSL: ${e.message}"))
        } catch (e: javax.net.ssl.SSLPeerUnverifiedException) {
            e.printStackTrace()
            Result.failure(Exception("Error SSL hostname: ${e.message}"))
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure(Exception("Error de xarxa: ${e.javaClass.simpleName} - ${e.message}"))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("Error inesperat: ${e.javaClass.simpleName} - ${e.message}"))
        }
    }

    /**
     * Realitza el tancament de sessió de l'usuari autenticat.
     *
     * Envia al servidor l'identificador de sessió actiu per invalidar-lo
     * i finalitzar correctament la sessió.
     *
     * @param sessionId Identificador de sessió de l'usuari autenticat
     *
     * @return [Result] amb:
     * - [Unit] si el logout és correcte
     * - Excepció amb missatge descriptiu si hi ha error
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
     * Registra un nou alumne al sistema.
     *
     * Envia al backend les dades del formulari de registre i,
     * si el procés finalitza correctament, retorna l'usuari
     * autenticat amb la sessió iniciada.
     *
     * @param request Dades necessàries per al registre de l'alumne
     *
     * @return [Result] amb:
     * - [User] si el registre és correcte
     * - Excepció amb missatge descriptiu si hi ha error
     */
    open suspend fun registerStudent(
        request: RegisterRequest
    ): Result<User> {
        return try {
            val response = apiService.registerStudent(request)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val user = User(
                        username = request.username,
                        role = mapUserType(body.userType),
                        sessionId = body.sessionId
                    )
                    Result.success(user)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    400 -> Result.failure(Exception("L'usuari o correu electrònic ja existeix"))
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
     * Converteix el tipus d'usuari retornat pel backend
     * al rol intern utilitzat per l'aplicació.
     *
     * Aquesta conversió permet desacoblar els valors rebuts
     * del servidor dels valors interns utilitzats a la UI.
     *
     * @param userType Tipus d'usuari retornat pel backend:
     * - student
     * - company
     * - admin
     *
     * @return Rol equivalent dins l'aplicació:
     * - ALUMNE
     * - EMPRESA
     * - ADMIN
     * - DESCONEGUT si no coincideix amb cap valor esperat
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