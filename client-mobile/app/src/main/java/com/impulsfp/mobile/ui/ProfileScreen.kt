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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.SessionData

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit,
    menuViewModel: MenuViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profile = profileViewModel.profile
    val context = LocalContext.current
    val fullName = "${profile.name} ${profile.surname}".trim()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deletePassword by remember { mutableStateOf("") }
    var deleteError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val sessionId = SessionData.getSessionId()
        if (sessionId != null) {
            profileViewModel.refreshProfile(sessionId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppTopBar(
            name = profile.name,
            avatarId = profile.avatarId,
            onHomeClick = onHomeClick,
            onApplicationsClick = { },
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
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(getAvatarColor(profile.avatarId)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar perfil")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    deletePassword = ""
                    deleteError = null
                    showDeleteDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar compte")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Eliminar compte")
            },
            text = {
                Column {
                    Text("Introdueix la teva contrasenya per confirmar l'eliminació del compte.")

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = deletePassword,
                        onValueChange = {
                            deletePassword = it
                            deleteError = null
                        },
                        label = { Text("Contrasenya") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    deleteError?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val sessionId = SessionData.getSessionId()

                        if (sessionId == null) {
                            deleteError = "No hi ha cap sessió activa"
                            return@TextButton
                        }

                        if (deletePassword.isBlank()) {
                            deleteError = "Has d'introduir la contrasenya"
                            return@TextButton
                        }

                        profileViewModel.deleteAccount(
                            sessionId = sessionId,
                            password = deletePassword,
                            onSuccess = {
                                showDeleteDialog = false
                                onLogout()
                            },
                            onError = { error ->
                                deleteError = error
                            }
                        )
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel·lar")
                }
            }
        )
    }
}

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
            text = value.ifBlank { "No especificat" },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

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

private fun getAvatarColor(avatarId: Int): Color {
    return when (avatarId) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFFFF9800)
        else -> Color(0xFF9C27B0)
    }
}