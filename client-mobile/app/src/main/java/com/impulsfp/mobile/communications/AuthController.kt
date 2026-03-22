package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.User
import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.LoginRequest

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
     * @param username Nom d'usuari introduït a la pantalla del login
     * @param password Contrasenya introduïda a la pantalla del login
     *
     * @return Result<User>
     *     Si tot va bé retorna un User amb:
     *     - username: nom introduït per l'usuari
     *     - role: rol intern de l'app (ALUMNE, EMPRESA, ADMIN)
     *     - sessionId: sessió retornada pel servidor
     *
     *     Si hi ha error, retorna una excepció amb un missatge explicatiu.
     */
    open suspend fun login (username: String, password: String): Result<User> {
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
                Result.failure(Exception("Usuari o contrasenya incorrectes"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de connexió amb el servidor"))
        }
    }

    /**
     * Realitza el logout contra el servidor utilitzant l'identificador de sessió
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
                Result.failure(Exception("Sessió no vàlida"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de connexió amb el servidor"))
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