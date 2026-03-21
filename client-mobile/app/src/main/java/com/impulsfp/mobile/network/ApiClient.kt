package com.impulsfp.mobile.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objecte encarregat de configurar Retrofit i proporcionar
 * les instàncies dels serveis de xarxa de l'aplicació.
 *
 * La URL base es pot canviar per adaptar-se a diferents entorns:
 * - emulador Android: http://10.0.2.2:8080/
 * - tests locals JUnit: http://localhost:8080/
 */

object ApiClient {

    /**
     * URL base per defecte de l'aplicació.
     * En execució normal sobre l'emulador Android, 10.0.2.2 apunta
     * al localhost de la màquina host.
     */

    private var baseUrl: String = "http://10.0.2.2:8080/"

    /**
     * Instància única de Retrofit configurada amb:
     * -URL base del servidor
     * -conversor Gson per transformar JSON en objectes Kotlin
     */
    private var retrofit: Retrofit? = null

    /**
     * Permet canviar la URL base del servidor. Quan canvia,
     * es reinicialitza Retrofit
     * @param url nova URL base
     */
    fun setBaseUrl(url: String) {
        baseUrl = url
        retrofit = null
    }

    /**
     * Retorna la isntància de Retrofit, creant-la si cal.
     */
    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    /**
     * Servei d'autenticació utilitzat per fer les peticions
     * de login i logout al servidor
     */
    val authApiService: AuthApiService
        get() = getRetrofit().create(AuthApiService::class.java)

}