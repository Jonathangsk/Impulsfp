package com.impulsfp.mobile.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.SessionData

/**
 * Pantalla de visualització del perfil d'usuari.
 *
 * Aquesta pantalla mostra la informació personal, acadèmica
 * i professional de l'usuari autenticat, organitzada en diferents
 * seccions visuals.
 *
 * També permet accedir a funcionalitats relacionades com:
 * - editar el perfil
 * - obrir el portafolis en un navegador extern
 * - navegar a altres pantalles principals de l'aplicació
 * - tancar la sessió
 *
 * Quan la pantalla es carrega, es refresquen les dades del perfil
 * a partir de la sessió activa.
 *
 * @param onHomeClick Funció executada en prémer l'accés a inici
 * @param onEditProfile Funció executada en prémer el botó d'editar perfil
 * @param onApplicationsClick Funció executada en prémer l'accés a candidatures
 * @param onLogout Funció executada quan l'usuari tanca la sessió
 * @param profileViewModel ViewModel encarregat de gestionar les dades del perfil
 * @param menuViewModel ViewModel encarregat de gestionar accions del menú, com el logout
 *
 * @author abenitez
 */
@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onEditProfile: () -> Unit,
    onApplicationsClick: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel,
    menuViewModel: MenuViewModel = viewModel()
) {
    val profile = profileViewModel.profile
    val context = LocalContext.current
    val fullName = "${profile.name} ${profile.surname}".trim()

    LaunchedEffect(Unit) {
        val sessionId = SessionData.getSessionId()
        if (sessionId != null) {
            profileViewModel.refreshProfile(sessionId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profileScreen")
    ) {
        AppTopBar(
            name = profile.name,
            onHomeClick = onHomeClick,
            onApplicationsClick = onApplicationsClick,
            onProfileClick = { },
            onLogoutClick = {
                menuViewModel.logout {
                    onLogout()
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Perfil d'usuari",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = fullName.ifBlank { "Usuari" },
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = profile.email.ifBlank { "Sense correu" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileSectionCard(
                title = "Informació personal"
            ) {
                ProfileRow("Nom d'usuari", profile.username)
                ProfileRow("Nom", profile.name)
                ProfileRow("Cognoms", profile.surname)
                ProfileRow("Email", profile.email)
                ProfileRow("Telèfon", profile.phoneNumber)
                ProfileRow("Ciutat", profile.city)
                ProfileRow("Biografia", profile.bio)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSectionCard(
                title = "Perfil professional"
            ) {
                ProfileRow("Cicle Formatiu", profile.cycle)
                ProfileRow("Nivell", profile.experienceLevel)

                ProfileChipSection(
                    title = "Skills",
                    items = profile.skills
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileChipSection(
                    title = "Idiomes",
                    items = profile.languages
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSectionCard(
                title = "Preferències"
            ) {
                ProfileChipSection(
                    title = "Rols preferits",
                    items = profile.preferredRoles
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileRow("Ubicació preferida", profile.preferredLocation)
                ProfileRow("Disponibilitat", profile.availability)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (profile.portfolio.isNotBlank() && profile.portfolio.startsWith("http")) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.portfolio))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Veure portfolio")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = onEditProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("editProfileButton")
            ) {
                Text("Editar perfil")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Targeta reutilitzable per agrupar una secció d'informació
 * dins la pantalla de perfil.
 *
 * Aquest component mostra un títol i un contingut composable
 * personalitzat, mantenint un estil visual coherent entre
 * les diferents seccions.
 *
 * @param title Títol de la secció
 * @param content Contingut composable que es mostrarà dins la targeta
 */
@Composable
private fun ProfileSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(14.dp))

            content()
        }
    }
}

/**
 * Mostra una fila d'informació textual del perfil.
 *
 * Aquest component s'utilitza per presentar una etiqueta
 * i el seu valor associat dins les seccions del perfil.
 *
 * @param label Nom del camp que es mostra
 * @param value Valor associat al camp
 */
@Composable
private fun ProfileRow(label: String, value: String) {
    Column(
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value.ifBlank { "" },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Mostra una secció de valors en format xip.
 *
 * Aquest component s'utilitza per representar llistes com
 * habilitats, idiomes o rols preferits. Si la llista és buida,
 * es mostra un text indicant que no hi ha informació especificada.
 *
 * @param title Títol de la secció
 * @param items Llista d'elements a mostrar
 */
@Composable
private fun ProfileChipSection(
    title: String,
    items: List<String>
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (items.isEmpty()) {
        Text(
            text = "No especificat",
            style = MaterialTheme.typography.bodyLarge
        )
    } else {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}