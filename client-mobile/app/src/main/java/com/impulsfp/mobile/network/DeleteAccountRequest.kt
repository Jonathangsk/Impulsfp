package com.impulsfp.mobile.network

/**
 * Model de petició utilitzat per sol·licitar l'eliminació
 * del compte d'usuari al servidor.
 *
 * Aquesta classe encapsula la informació necessària perquè
 * el backend pugui validar la identitat de l'usuari abans
 * d'executar l'eliminació definitiva del compte.
 *
 * En aquest cas, es requereix la contrasenya actual com a
 * mesura de seguretat addicional.
 *
 * En tractar-se d'una `data class`, Kotlin genera automàticament
 * mètodes útils com `copy()`, `equals()`, `hashCode()` i `toString()`.
 *
 * @property password Contrasenya actual de l'usuari per confirmar l'acció
 *
 * @author abenitez
 */
data class DeleteAccountRequest(
    val password: String
)