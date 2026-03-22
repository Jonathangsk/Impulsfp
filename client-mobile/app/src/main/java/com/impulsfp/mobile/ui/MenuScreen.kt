package com.impulsfp.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.SessionData

/**
 * Pantalla principal de l'aplicació després del l'autenticació.
 *
 * Mostra informació bàsica de l'usuari autenticat i adapta el contingut
 * segons el seu rol (ADMIN, EMPRESA o ALUMNE).
 *
 * Permet també tancar la sessió mitjançant el botó de logout.
 *
 * Nota: Aquesta pantalla és provisional i es completarà en futurs Sprints.
 *
 * @param onLogout Funció que s'executa quan l'usuari tanca sessió
 * @param menuViewModel ViewModel encarregat de gestionar el procés de logout
 *
 * @author abenitez
 */
@Composable
fun MenuScreen(
    onLogout: () -> Unit,
    menuViewModel: MenuViewModel = viewModel()
) {
    val user = SessionData.currentUser

    val menuTitle = when (user?.role) {
        "ADMIN" -> "Menú administrador"
        "EMPRESA" -> "Menú empresa"
        "ALUMNE" -> "Menú alumne"
        else -> "Menú principal"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = menuTitle,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("Usuari: ${user?.username ?: "-"}")
            Text("Rol: ${user?.role ?: "-"}")

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    menuViewModel.logout {
                        onLogout()
                    }
                },
                modifier = Modifier.testTag("logoutButton")
            ) {
                Text("Tancar sessió")
            }
        }
    }
}