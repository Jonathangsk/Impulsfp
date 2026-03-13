package com.impulsfp.mobile.communications

import com.impulsfp.mobile.data.User
import kotlinx.coroutines.delay

class AuthController {
    suspend fun login (username: String, password: String): Result<User> {
        return try {
            delay(1000)

            /**
             * Proves temporals fins connectar amb Servidor i Base de dades
             */
            val user = when {
                username == "admin" && password == "1234" ->
                    User(
                        id = 1,
                        username = "admin",
                        role = "ADMIN"
                    )
                username == "empresa" && password == "1234" ->
                    User(
                        id = 2,
                        username = "empresa",
                        role = "EMPRESA"
                    )
                username == "alumne" && password == "1234" ->
                    User(
                        id = 3,
                        username = "alumne",
                        role = "ALUMNE"
                    )

                else -> null
            }

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Credencials incorrectes"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}