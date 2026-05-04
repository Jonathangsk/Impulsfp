package com.impulsfp.mobile.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.SessionData

/**
 * Pantalla d'edició del perfil d'usuari.
 *
 * Aquesta pantalla permet visualitzar i modificar les dades personals,
 * acadèmiques i professionals del perfil de l'usuari autenticat.
 *
 * També inclou funcionalitats complementàries com:
 * - desar els canvis del perfil
 * - obrir el diàleg per canviar la contrasenya
 * - obrir el diàleg per eliminar el compte
 * - navegar a les diferents seccions principals de l'aplicació
 *
 * La pantalla utilitza [ProfileViewModel] per gestionar l'estat
 * del perfil i les operacions de desament o eliminació.
 *
 * @param onHomeClick Funció executada en prémer l'accés a inici
 * @param onSaveSuccess Funció executada quan el perfil es desa correctament
 * @param onProfileClick Funció executada per tornar a la pantalla de perfil
 * @param onApplicationsClick Funció executada en prémer l'accés a candidatures
 * @param onLogout Funció executada quan l'usuari tanca la sessió
 * @param profileViewModel ViewModel encarregat de gestionar l'estat del perfil
 * @param menuViewModel ViewModel encarregat de gestionar accions del menú, com el logout
 *
 * @author abenitez
 */
@Composable
fun EditProfileScreen(
    onHomeClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    onProfileClick: () -> Unit,
    onApplicationsClick: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel,
    menuViewModel: MenuViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentProfile = profileViewModel.profile

    var username by remember(currentProfile.username) { mutableStateOf(currentProfile.username) }
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

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deletePassword by remember { mutableStateOf("") }
    var deleteError by remember { mutableStateOf<String?>(null) }

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var changePasswordError by remember { mutableStateOf<String?>(null) }
    var changePasswordInfo by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("editProfileScreen")
    ) {

        AppTopBar(
            name = name,
            onHomeClick = onHomeClick,
            onApplicationsClick = onApplicationsClick,
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

            Spacer(modifier = Modifier.height(12.dp))

            profileViewModel.saveError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            EditSectionCard(title = "Informació personal") {
                AppTextField(
                    value = username,
                    onValueChange = { },
                    label = "Nom d'usuari",
                    required = true,
                    isError = false,
                    errorText = null,
                    enabled = false
                )

                AppTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        profileViewModel.clearNameError()
                        profileViewModel.clearSaveState()
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
                        profileViewModel.clearSaveState()
                    },
                    label = "Cognoms",
                    required = true,
                    isError = profileViewModel.surnameError != null,
                    errorText = profileViewModel.surnameError
                )

                AppTextField(
                    value = email,
                    onValueChange = { },
                    label = "Email",
                    required = true,
                    isError = false,
                    errorText = null,
                    keyboardType = KeyboardType.Email,
                    enabled = false
                )

                AppTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Telèfon",
                    keyboardType = KeyboardType.Phone
                )

                AppTextField(
                    value = city,
                    onValueChange = {
                        city = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Ciutat"
                )

                AppTextField(
                    value = bio,
                    onValueChange = {
                        bio = it
                        profileViewModel.clearSaveState()
                    },
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
                        profileViewModel.clearSaveState()
                    },
                    label = "Cicle Formatiu",
                    required = true,
                    isError = profileViewModel.cycleError != null,
                    errorText = profileViewModel.cycleError
                )

                AppTextField(
                    value = experienceLevel,
                    onValueChange = {
                        experienceLevel = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Nivell d'experiència"
                )

                AppTextField(
                    value = skillsText,
                    onValueChange = {
                        skillsText = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Skills (separades per comes)",
                    singleLine = false
                )

                AppTextField(
                    value = languagesText,
                    onValueChange = {
                        languagesText = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Idiomes (separats per comes)",
                    singleLine = false
                )

                AppTextField(
                    value = portfolio,
                    onValueChange = {
                        portfolio = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Portfolio"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            EditSectionCard(title = "Preferències") {
                AppTextField(
                    value = preferredRolesText,
                    onValueChange = {
                        preferredRolesText = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Rols preferits (separats per comes)",
                    singleLine = false
                )

                AppTextField(
                    value = preferredLocation,
                    onValueChange = {
                        preferredLocation = it
                        profileViewModel.clearSaveState()
                    },
                    label = "Ubicació preferida"
                )

                AppTextField(
                    value = availability,
                    onValueChange = {
                        availability = it
                        profileViewModel.clearSaveState()
                    },
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
                    modifier = Modifier.weight(1f),
                    enabled = !profileViewModel.isLoading
                ) {
                    Text("Cancel·lar")
                }

                Button(
                    onClick = {
                        profileViewModel.saveProfile(
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
                            onSuccess = {
                                onSaveSuccess()
                            }
                        )
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !profileViewModel.isLoading
                ) {
                    Text(
                        if (profileViewModel.isLoading) "Guardant..."
                        else "Guardar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    currentPassword = ""
                    newPassword = ""
                    confirmNewPassword = ""
                    changePasswordError = null
                    changePasswordInfo = null
                    showChangePasswordDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !profileViewModel.isLoading
            ) {
                Text("Canviar contrasenya")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    deletePassword = ""
                    deleteError = null
                    showDeleteDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !profileViewModel.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                )
            ) {
                Text("Eliminar compte")
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showChangePasswordDialog) {
        AlertDialog(
            onDismissRequest = {
                showChangePasswordDialog = false
            },
            title = {
                Text("Canviar contrasenya")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = {
                            currentPassword = it
                            changePasswordError = null
                            changePasswordInfo = null
                        },
                        label = { Text("Contrasenya actual") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            changePasswordError = null
                            changePasswordInfo = null
                        },
                        label = { Text("Nova contrasenya") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = {
                            confirmNewPassword = it
                            changePasswordError = null
                            changePasswordInfo = null
                        },
                        label = { Text("Confirma la nova contrasenya") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "La nova contrasenya ha de contenir com a mínim 6 caràcters, 1 majúscula, 1 minúscula i 1 número.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    changePasswordError?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    changePasswordInfo?.let { info ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = info,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val passwordError = validatePasswordChange(
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                            confirmNewPassword = confirmNewPassword
                        )

                        if (passwordError != null) {
                            changePasswordError = passwordError
                            return@TextButton
                        }

                        val sessionId = SessionData.getSessionId()

                        if (sessionId == null) {
                            changePasswordError = "No hi ha cap sessió activa"
                            return@TextButton
                        }

                        profileViewModel.changePassword(
                            sessionId = sessionId,
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                            onSuccess = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                                currentPassword = ""
                                newPassword = ""
                                confirmNewPassword = ""
                                changePasswordError = null
                                changePasswordInfo = null
                                showChangePasswordDialog = false
                            },
                            onError = { error ->
                                changePasswordError = error
                            }
                        )
                    },
                    enabled = !profileViewModel.isLoading
                ) {
                    Text(
                        if (profileViewModel.isLoading) "Canviant..."
                        else "Confirmar"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showChangePasswordDialog = false
                    }
                ) {
                    Text("Cancel·lar")
                }
            }
        )
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
                    Text(
                        text = "Confirmar",
                        color = MaterialTheme.colorScheme.error
                    )
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

/**
 * Valida les dades introduïdes al formulari de canvi de contrasenya.
 *
 * Aquesta funció comprova que:
 * - la contrasenya actual no sigui buida
 * - la nova contrasenya compleixi els requisits mínims de seguretat
 * - la confirmació no sigui buida
 * - la nova contrasenya i la confirmació coincideixin
 * - la nova contrasenya sigui diferent de l'actual
 *
 * @param currentPassword Contrasenya actual de l'usuari
 * @param newPassword Nova contrasenya proposada
 * @param confirmNewPassword Confirmació de la nova contrasenya
 *
 * @return Missatge d'error si la validació falla o `null` si és correcta
 */
private fun validatePasswordChange(
    currentPassword: String,
    newPassword: String,
    confirmNewPassword: String
): String? {
    if (currentPassword.isBlank()) {
        return "Has d'introduir la contrasenya actual."
    }

    if (newPassword.isBlank()) {
        return "Has d'introduir la nova contrasenya."
    }

    val requirements = mutableListOf<String>()

    if (newPassword.length < 6) {
        requirements.add("• mínim 6 caràcters")
    }
    if (!newPassword.any { it.isUpperCase() }) {
        requirements.add("• 1 majúscula")
    }
    if (!newPassword.any { it.isLowerCase() }) {
        requirements.add("• 1 minúscula")
    }
    if (!newPassword.any { it.isDigit() }) {
        requirements.add("• 1 número")
    }

    if (requirements.isNotEmpty()) {
        return "La nova contrasenya ha de contenir:\n${requirements.joinToString("\n")}"
    }

    if (confirmNewPassword.isBlank()) {
        return "Has de confirmar la nova contrasenya."
    }

    if (newPassword != confirmNewPassword) {
        return "Les contrasenyes no coincideixen."
    }

    if (currentPassword == newPassword) {
        return "La nova contrasenya ha de ser diferent de l'actual."
    }

    return null
}

/**
 * Targeta reutilitzable per agrupar seccions del formulari d'edició.
 *
 * Aquest component encapsula un bloc visual amb un títol i un contingut
 * personalitzat, utilitzat per separar la informació personal, el perfil
 * professional i les preferències.
 *
 * @param title Títol de la secció
 * @param content Contingut composable que es mostrarà dins la targeta
 */
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

/**
 * Camp de text reutilitzable per al formulari d'edició del perfil.
 *
 * Aquest component encapsula un [OutlinedTextField] amb suport per:
 * - etiqueta amb indicador de camp obligatori
 * - missatge d'error
 * - configuració del teclat
 * - activació o desactivació del camp
 *
 * @param value Valor actual del camp
 * @param onValueChange Funció executada quan canvia el valor
 * @param label Text de l'etiqueta del camp
 * @param required Indica si el camp és obligatori
 * @param singleLine Indica si el camp ha de ser d'una sola línia
 * @param isError Indica si el camp es mostra en estat d'error
 * @param errorText Missatge d'error associat al camp
 * @param keyboardType Tipus de teclat a mostrar
 * @param enabled Indica si el camp es pot editar
 */
@Composable
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    required: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
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
        enabled = enabled,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
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

/**
 * Etiqueta reutilitzable per mostrar el nom d'un camp de formulari
 * amb indicació visual si és obligatori.
 *
 * Quan [required] és `true`, s'afegeix un asterisc en color d'error
 * al costat del text principal.
 *
 * @param text Text base de l'etiqueta
 * @param required Indica si el camp és obligatori
 */
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