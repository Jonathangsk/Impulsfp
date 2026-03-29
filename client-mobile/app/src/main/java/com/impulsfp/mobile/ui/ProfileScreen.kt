package com.impulsfp.mobile.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.ProfileRepository

/**
 * Pantalla de consulta del perfil.
 */
@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
    menuViewModel: MenuViewModel = viewModel()
) {
    val profile = ProfileRepository.getProfile()
    val context = LocalContext.current
    val fullName = "${profile.name} ${profile.surname}".trim()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppTopBar(
            name = profile.name,
            avatarId = profile.avatarId,
            onHomeClick = onHomeClick,
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
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Perfil d'usuari",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(getAvatarColor(profile.avatarId))
                    .padding(28.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.name.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dades del perfil
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileRow("Nom", profile.name)
                    ProfileRow("Cognoms", profile.surname)
                    ProfileRow("Nom complet", fullName)
                    ProfileRow("Email", profile.email)
                    ProfileRow("Telèfon", profile.phoneNumber)
                    ProfileRow("Ciutat", profile.city)
                    ProfileRow("Biografia", profile.bio)
                    ProfileRow("Cicle", profile.cycle)
                    ProfileRow("Skills", profile.skills.joinToString(", "))
                    ProfileRow("Nivell", profile.experienceLevel)
                    ProfileRow("Idiomes", profile.languages.joinToString(", "))
                    ProfileRow("Rols preferits", profile.preferredRoles.joinToString(", "))
                    ProfileRow("Ubicació preferida", profile.preferredLocation)
                    ProfileRow("Disponibilitat", profile.availability)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔗 BOTÓ PORTFOLIO
            if (profile.portfolio.isNotBlank() && profile.portfolio.startsWith("http")) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.portfolio))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Veure portfolio")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botó editar
            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar perfil")
            }
        }
    }
}

/**
 * Fila de dades del perfil
 */
@Composable
private fun ProfileRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = value.ifBlank { "No especificat" },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Color avatar
 */
private fun getAvatarColor(avatarId: Int): Color {
    return when (avatarId) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFFFF9800)
        else -> Color(0xFF9C27B0)
    }
}