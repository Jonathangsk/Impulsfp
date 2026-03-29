package com.impulsfp.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.ProfileRepository

/**
 * Pantalla d'edició del perfil d'usuari.
 *
 * Permet modificar les dades principals del perfil i canviar
 * l'avatar de manera simple.
 *
 * La pantalla utilitza la TopBar comuna de l'aplicació:
 * - logo: torna a la pantalla principal
 * - avatar: permet mantenir l'accés al perfil
 * - logout: tanca la sessió
 *
 * @param onHomeClick Funció que redirigeix a la pantalla principal
 * @param onSaveSuccess Funció que s'executa després de guardar correctament
 * @param onProfileClick Funció que redirigeix a la pantalla de perfil
 * @param onLogout Funció que s'executa quan l'usuari tanca sessió
 * @param menuViewModel ViewModel encarregat de gestionar el procés de logout
 */
@Composable
fun EditProfileScreen(
    onHomeClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit,
    menuViewModel: MenuViewModel = viewModel()
) {
    val currentProfile = ProfileRepository.getProfile()

    var name by remember { mutableStateOf(currentProfile.name) }
    var surname by remember { mutableStateOf(currentProfile.surname) }
    var email by remember { mutableStateOf(currentProfile.email) }
    var phoneNumber by remember { mutableStateOf(currentProfile.phoneNumber) }
    var city by remember { mutableStateOf(currentProfile.city) }
    var bio by remember { mutableStateOf(currentProfile.bio) }
    var cicle by remember { mutableStateOf(currentProfile.cycle) }
    var skillsText by remember { mutableStateOf(currentProfile.skills.joinToString(", ")) }
    var experienceLevel by remember { mutableStateOf(currentProfile.experienceLevel) }
    var languagesText by remember { mutableStateOf(currentProfile.languages.joinToString(", ")) }
    var preferredRolesText by remember { mutableStateOf(currentProfile.preferredRoles.joinToString(", ")) }
    var preferredLocation by remember { mutableStateOf(currentProfile.preferredLocation) }
    var availability by remember { mutableStateOf(currentProfile.availability) }
    var portfolio by remember { mutableStateOf(currentProfile.portfolio) }
    var avatarId by remember { mutableIntStateOf(currentProfile.avatarId) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppTopBar(
            name = name,
            avatarId = avatarId,
            onHomeClick = onHomeClick,
            onProfileClick = onProfileClick,
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
                text = "Editar perfil",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(getEditAvatarColor(avatarId))
                    .padding(28.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(1).ifEmpty { "?" }.uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    avatarId = when (avatarId) {
                        1 -> 2
                        2 -> 3
                        3 -> 4
                        else -> 1
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Canviar avatar")
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Cognoms") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Telèfon") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Ciutat") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Biografia") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = cicle,
                onValueChange = { cicle = it },
                label = { Text("Cicle") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = skillsText,
                onValueChange = { skillsText = it },
                label = { Text("Skills (separades per comes)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = experienceLevel,
                onValueChange = { experienceLevel = it },
                label = { Text("Nivell d'experiència") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = languagesText,
                onValueChange = { languagesText = it },
                label = { Text("Idiomes (separats per comes)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = preferredRolesText,
                onValueChange = { preferredRolesText = it },
                label = { Text("Rols preferits (separats per comes)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = preferredLocation,
                onValueChange = { preferredLocation = it },
                label = { Text("Ubicació preferida") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = availability,
                onValueChange = { availability = it },
                label = { Text("Disponibilitat") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = portfolio,
                onValueChange = { portfolio = it },
                label = { Text("Portfolio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onProfileClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel·lar")
                }

                Button(
                    onClick = {
                        ProfileRepository.updateProfile(
                            currentProfile.copy(
                                name = name.trim(),
                                surname = surname.trim(),
                                email = email.trim(),
                                phoneNumber = phoneNumber.trim(),
                                city = city.trim(),
                                bio = bio.trim(),
                                cycle = cicle.trim(),
                                skills = skillsText.toListFromCommaText(),
                                experienceLevel = experienceLevel.trim(),
                                languages = languagesText.toListFromCommaText(),
                                preferredRoles = preferredRolesText.toListFromCommaText(),
                                preferredLocation = preferredLocation.trim(),
                                availability = availability.trim(),
                                portfolio = portfolio.trim(),
                                avatarId = avatarId
                            )
                        )
                        onSaveSuccess()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Converteix un text separat per comes en una llista neta.
 */
private fun String.toListFromCommaText(): List<String> {
    return split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
}

/**
 * Retorna un color segons l'identificador d'avatar seleccionat.
 *
 * @param avatarId Identificador de l'avatar
 * @return Color associat a l'avatar
 */
private fun getEditAvatarColor(avatarId: Int): Color {
    return when (avatarId) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFFFF9800)
        else -> Color(0xFF9C27B0)
    }
}