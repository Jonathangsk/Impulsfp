package com.impulsfp.mobile.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objecte encarregat de configurar Retrofit i proporcionar
 * les instàncies dels serveis de xarxa de l'aplicació.
 *
 * Gestiona la creació d'una instància única de Retrofit i permet
 * modificar la URL base per adaptar-se a diferents entorns.
 *
 * Exemples de configuració:
 * - Emulador Android: http://10.0.2.2:8080/
 * - tests locals (JUnit): http://localhost:8080/
 *
 * @author abenitez
 */

object ApiClient {

    /**
     * URL base per defecte de l'aplicació.
     *
     * En execució normal sobre l'emulador Android, 10.0.2.2 apunta
     * al localhost de la màquina host.
     */

    private var baseUrl: String = "http://10.0.2.2:8080/" //Servidor en local
    //private var baseUrl: String = "http://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/" //Servidor en Isard

    /**
     * Instància única de Retrofit configurada amb:
     * -URL base del servidor
     * -conversor Gson per transformar JSON en objectes Kotlin
     */
    private var retrofit: Retrofit? = null

    /**
     * Permet canviar la URL base del servidor.
     *
     * Quan es modifica, es reinicialitza la instància de Retrofit
     * per aplicar la nova configuració.
     *
     * @param url nova URL base del servidor
     */
    fun setBaseUrl(url: String) {
        baseUrl = url
        retrofit = null
    }

    /**
     * Retorna la isntància de Retrofit, creant-la si cal.
     *
     * @return Instància de Retrofit configurada amb la URL base actual
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