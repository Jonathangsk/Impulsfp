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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditProfileScreen(
    onHomeClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit,
    menuViewModel: MenuViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val currentProfile = profileViewModel.profile

    var name by remember(currentProfile.name) { mutableStateOf(currentProfile.name) }
    var surname by remember(currentProfile.surname) { mutableStateOf(currentProfile.surname) }
    var email by remember(currentProfile.email) { mutableStateOf(currentProfile.email) }
    var phoneNumber by remember(currentProfile.phoneNumber) { mutableStateOf(currentProfile.phoneNumber) }
    var city by remember(currentProfile.city) { mutableStateOf(currentProfile.city) }
    var bio by remember(currentProfile.bio) { mutableStateOf(currentProfile.bio) }
    var cycle by remember(currentProfile.cycle) { mutableStateOf(currentProfile.cycle) }
    var skillsText by remember(currentProfile.skills) {
        mutableStateOf(currentProfile.skills.joinToString(", "))
    }
    var experienceLevel by remember(currentProfile.experienceLevel) {
        mutableStateOf(currentProfile.experienceLevel)
    }
    var languagesText by remember(currentProfile.languages) {
        mutableStateOf(currentProfile.languages.joinToString(", "))
    }
    var preferredRolesText by remember(currentProfile.preferredRoles) {
        mutableStateOf(currentProfile.preferredRoles.joinToString(", "))
    }
    var preferredLocation by remember(currentProfile.preferredLocation) {
        mutableStateOf(currentProfile.preferredLocation)
    }
    var availability by remember(currentProfile.availability) {
        mutableStateOf(currentProfile.availability)
    }
    var portfolio by remember(currentProfile.portfolio) {
        mutableStateOf(currentProfile.portfolio)
    }
    var avatarId by remember(currentProfile.avatarId) { mutableIntStateOf(currentProfile.avatarId) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppTopBar(
            name = name,
            avatarId = avatarId,
            onHomeClick = onHomeClick,
            onApplicationsClick = { },
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "* Camps obligatoris",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
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
                        .background(getEditAvatarColor(avatarId)),
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
                    }
                ) {
                    Text("Canviar avatar")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            EditSectionCard(title = "Informació personal") {
                AppTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        profileViewModel.clearNameError()
                    },
                    label = "Nom",
                    required = true,
                    isError = profileViewModel.nameError != null,
                    errorText = profileViewModel.nameError
                )

                AppTextField(
                    value = surname,
                    onValueChange = {
                        surname = it
                        profileViewModel.clearSurnameError()
                    },
                    label = "Cognoms",
                    required = true,
                    isError = profileViewModel.surnameError != null,
                    errorText = profileViewModel.surnameError
                )

                AppTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        profileViewModel.clearEmailError()
                    },
                    label = "Email",
                    required = true,
                    isError = profileViewModel.emailError != null,
                    errorText = profileViewModel.emailError,
                    keyboardType = KeyboardType.Email
                )

                AppTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Telèfon",
                    required = false,
                    keyboardType = KeyboardType.Phone
                )

                AppTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = "Ciutat"
                )

                AppTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = "Biografia",
                    singleLine = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EditSectionCard(title = "Perfil professional") {
                AppTextField(
                    value = cycle,
                    onValueChange = {
                        cycle = it
                        profileViewModel.clearCycleError()
                    },
                    label = "Cicle Formatiu",
                    required = true,
                    isError = profileViewModel.cycleError != null,
                    errorText = profileViewModel.cycleError
                )

                AppTextField(
                    value = experienceLevel,
                    onValueChange = { experienceLevel = it },
                    label = "Nivell d'experiència"
                )

                AppTextField(
                    value = skillsText,
                    onValueChange = { skillsText = it },
                    label = "Skills (separades per comes)",
                    singleLine = false
                )

                AppTextField(
                    value = languagesText,
                    onValueChange = { languagesText = it },
                    label = "Idiomes (separats per comes)",
                    singleLine = false
                )

                AppTextField(
                    value = portfolio,
                    onValueChange = { portfolio = it },
                    label = "Portfolio"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EditSectionCard(title = "Preferències") {
                AppTextField(
                    value = preferredRolesText,
                    onValueChange = { preferredRolesText = it },
                    label = "Rols preferits (separats per comes)",
                    singleLine = false
                )

                AppTextField(
                    value = preferredLocation,
                    onValueChange = { preferredLocation = it },
                    label = "Ubicació preferida"
                )

                AppTextField(
                    value = availability,
                    onValueChange = { availability = it },
                    label = "Disponibilitat"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onProfileClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel·lar")
                }

                Button(
                    onClick = {
                        val saved = profileViewModel.saveProfile(
                            name = name,
                            surname = surname,
                            email = email,
                            phoneNumber = phoneNumber,
                            city = city,
                            bio = bio,
                            cycle = cycle,
                            skillsText = skillsText,
                            experienceLevel = experienceLevel,
                            languagesText = languagesText,
                            preferredRolesText = preferredRolesText,
                            preferredLocation = preferredLocation,
                            availability = availability,
                            portfolio = portfolio,
                            avatarId = avatarId
                        )

                        if (saved) {
                            onSaveSuccess()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun EditSectionCard(
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
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    required: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            RequiredLabel(
                text = label,
                required = required
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        supportingText = {
            if (!errorText.isNullOrBlank()) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun RequiredLabel(
    text: String,
    required: Boolean
) {
    Text(
        buildAnnotatedString {
            append(text)
            if (required) {
                append(" ")
                withStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.error)
                ) {
                    append("*")
                }
            }
        }
    )
}

private fun getEditAvatarColor(avatarId: Int): Color {
    return when (avatarId) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFFFF9800)
        else -> Color(0xFF9C27B0)
    }
}