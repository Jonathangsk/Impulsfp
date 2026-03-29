package com.impulsfp.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.ProfileRepository
import com.impulsfp.mobile.data.SessionData

/**
 * Pantalla principal de l'aplicació després de l'autenticació.
 *
 * Mostra una estructura comuna amb TopBar i una àrea central de contingut.
 * Aquesta pantalla actuarà com a pantalla principal de l'usuari i, en futurs
 * sprints, mostrarà la llista d'ofertes de pràctiques.
 *
 * Actualment:
 * - mostra la TopBar comuna de l'aplicació
 * - permet accedir al perfil des de l'avatar
 * - permet tancar sessió des de la icona de logout
 * - mostra un contingut provisional a l'àrea principal
 *
 * El contingut es pot adaptar segons el rol de l'usuari autenticat
 * (ADMIN, EMPRESA o ALUMNE).
 *
 * @param onLogout Funció que s'executa quan l'usuari tanca sessió
 * @param onProfileClick Funció que s'executa quan l'usuari prem l'avatar
 * @param menuViewModel ViewModel encarregat de gestionar el procés de logout
 */
@Composable
fun MenuScreen(
    onLogout: () -> Unit,
    onProfileClick: () -> Unit,
    menuViewModel: MenuViewModel = viewModel()
) {
    val user = SessionData.currentUser
    val profile = ProfileRepository.getProfile()

    val menuTitle = when (user?.role) {
        "ADMIN" -> "Menú administrador"
        "EMPRESA" -> "Menú empresa"
        "ALUMNE" -> "Menú alumne"
        else -> "Menú principal"
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        /**
         * Barra superior comuna de l'aplicació.
         * - El logo redirigeix a la pantalla principal.
         * - L'avatar permet accedir al perfil.
         * - La icona de logout permet tancar sessió.
         *
         * En aquesta mateixa pantalla, onHomeClick no necessita cap acció
         * addicional perquè l'usuari ja es troba a la home.
         */
        AppTopBar(
            name = profile.name,
            avatarId = profile.avatarId,
            onHomeClick = { },
            onProfileClick = onProfileClick,
            onLogoutClick = {
                menuViewModel.logout {
                    onLogout()
                }
            }
        )

        /**
         * Àrea principal de contingut.
         *
         * En aquesta zona es mostrarà el llistat d'ofertes en
         * futurs passos del desenvolupament.
         *
         * Ara mostra informació provisional.
         */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = menuTitle,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Nom: ${profile.name} ${profile.surname}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Email: ${profile.email}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Rol: ${user?.role ?: "-"}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mainContentCard")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Contingut provisional",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Pantalla en construcció [...]",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}