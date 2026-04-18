package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.ApplicationUiModel
import com.impulsfp.mobile.network.ApiClient
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class ApplicationsController {

    private val apiService = ApiClient.applicationsApiService

    open suspend fun getApplications(sessionId: String): Result<List<ApplicationUiModel>> {
        return try {
            val response = apiService.getApplications(sessionId)

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    val applications = body.map { application ->
                        ApplicationUiModel(
                            id = application.id,
                            offerTitle = application.offerTitle,
                            companyName = application.companyName,
                            location = application.location,
                            status = application.status,
                            appliedAt = application.appliedAt
                        )
                    }

                    Result.success(applications)
                } else {
                    Result.failure(Exception("Resposta buida del servidor"))
                }
            } else {
                when (response.code()) {
                    401, 403 -> Result.failure(Exception("Sessió no vàlida"))
                    404 -> Result.failure(Exception("No s'han trobat candidatures"))
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