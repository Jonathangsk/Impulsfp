package com.impulsfp.mobile.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Objecte encarregat de configurar Retrofit i proporcionar
 * les instàncies dels serveis de xarxa de l'aplicació.
 *
 * Aquesta classe centralitza la configuració de comunicació
 * amb el backend mitjançant una única instància de Retrofit.
 *
 * També permet modificar la URL base del servidor per adaptar
 * l'aplicació a diferents entorns d'execució, com desenvolupament,
 * proves locals o servidor remot.
 *
 * Exemples de configuració:
 * - Emulador Android: http://10.0.2.2:8080/
 * - Tests locals (JUnit): http://localhost:8080/
 * - Servidor remot: URL pública configurada al projecte
 *
 * En utilitzar un `object`, Kotlin garanteix una única instància
 * compartida durant tota l'execució de l'aplicació.
 *
 * @author abenitez
 */
object ApiClient {

    /**
     * URL base utilitzada per connectar amb el backend.
     *
     * Aquesta adreça es pot modificar dinàmicament mitjançant
     * el mètode [setBaseUrl].
     *
     * En emulador Android, `10.0.2.2` representa el localhost
     * de la màquina host.
     */

    //private var baseUrl: String = "http://10.0.2.2:8080/" //Servidor en local
    private var baseUrl: String = "https://0bb0dfb7-9b4c-40bc-a0be.5b8c35470a40.bastion.elmeuescriptori.cat/" //Servidor en Isard

    /**
     * Instància única de Retrofit.
     *
     * Es crea de manera lazy quan es necessita per primera vegada
     * i es reinicialitza si canvia la URL base.
     */
    private var retrofit: Retrofit? = null

    /**
     * Actualitza la URL base del servidor.
     *
     * En modificar aquesta configuració, es reinicia la instància
     * actual de Retrofit per forçar la creació d'una nova connexió
     * amb la nova adreça configurada.
     *
     * @param url Nova URL base del servidor
     */
    fun setBaseUrl(url: String) {
        baseUrl = url
        retrofit = null
    }

    /**
     * Retorna la instància configurada de Retrofit.
     *
     * Si encara no existeix, es crea automàticament amb:
     * - la URL base actual
     * - el convertidor Gson per serialitzar/deserialitzar JSON
     *
     * @return Instància única de [Retrofit]
     */
    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    /**
     * Servei de xarxa per a operacions d'autenticació.
     *
     * Inclou funcionalitats com login, logout i registre.
     */
    val authApiService: AuthApiService
        get() = getRetrofit().create(AuthApiService::class.java)

    /**
     * Servei de xarxa per a operacions del perfil d'usuari.
     *
     * Inclou consulta, edició i eliminació del compte.
     */
    val profileApiService: ProfileApiService
        get() = getRetrofit().create(ProfileApiService::class.java)

    /**
     * Servei de xarxa per gestionar les ofertes laborals.
     *
     * Inclou consulta d'ofertes i accions relacionades.
     */
    val offersApiService: OffersApiService
        get() = getRetrofit().create(OffersApiService::class.java)

    /**
     * Servei de xarxa per gestionar les candidatures.
     *
     * Inclou consulta i registre de sol·licituds enviades.
     */
    val applicationsApiService: ApplicationsApiService
        get() = getRetrofit().create(ApplicationsApiService::class.java)

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory = sslContext.socketFactory
        val trustManager = trustAllCerts[0] as X509TrustManager

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }
}