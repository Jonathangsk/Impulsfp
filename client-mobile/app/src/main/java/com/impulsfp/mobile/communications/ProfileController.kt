package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.UserProfile
import com.impulsfp.mobile.network.ApiClient
import com.impulsfp.mobile.network.DeleteAccountRequest
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class ProfileController {

    private val apiService = ApiClient.profileApiService

    open suspend fun getProfile(sessionId: String): Result<UserProfile> {
        return try {
            val response = apiService.getProfile(sessionId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val profile = UserProfile(
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
                        portfolio = body.portfolio ?: "",
                        avatarId = body.avatarId ?: 1
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