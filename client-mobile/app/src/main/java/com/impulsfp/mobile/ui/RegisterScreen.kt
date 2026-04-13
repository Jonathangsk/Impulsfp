package com.impulsfp.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.R

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val hasValidationErrors =
        uiState.nameError != null ||
                uiState.surnameError != null ||
                uiState.emailError != null ||
                uiState.passwordError != null ||
                uiState.confirmPasswordError != null

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            onRegisterSuccess()
            registerViewModel.resetRegisterSuccess()
        }
    }

    LaunchedEffect(
        uiState.usernameError,
        uiState.nameError,
        uiState.surnameError,
        uiState.emailError,
        uiState.passwordError,
        uiState.confirmPasswordError,
        uiState.serverError
    ) {
        val firstErrorIndex = when {
            uiState.usernameError != null -> 3
            uiState.nameError != null -> 4
            uiState.surnameError != null -> 5
            uiState.emailError != null -> 6
            uiState.passwordError != null -> 7
            uiState.confirmPasswordError != null -> 8
            uiState.serverError != null -> 22
            else -> null
        }

        firstErrorIndex?.let {
            listState.animateScrollToItem(it)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo ImpulsFP",
                modifier = Modifier.size(200.dp)
            )
        }

        item {
            Text(
                text = "Crea el teu compte",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }

        item {
            Text(
                text = "* Camps obligatoris",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            RegisterTextField(
                value = uiState.username,
                onValueChange = { registerViewModel.onUsernameChange(it) },
                label = "Nom d'usuari",
                required = true,
                isError = uiState.usernameError != null,
                errorText = uiState.usernameError,
                modifier = Modifier.testTag("usernameField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.name,
                onValueChange = { registerViewModel.onNameChange(it) },
                label = "Nom",
                required = true,
                isError = uiState.nameError != null,
                errorText = uiState.nameError,
                modifier = Modifier.testTag("nameField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.surname,
                onValueChange = { registerViewModel.onSurnameChange(it) },
                label = "Cognoms",
                required = true,
                isError = uiState.surnameError != null,
                errorText = uiState.surnameError,
                modifier = Modifier.testTag("surnameField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.email,
                onValueChange = { registerViewModel.onEmailChange(it) },
                label = "Correu electrònic",
                required = true,
                isError = uiState.emailError != null,
                errorText = uiState.emailError,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.testTag("emailField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.password,
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = "Contrasenya",
                required = true,
                isError = uiState.passwordError != null,
                errorText = uiState.passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                modifier = Modifier.testTag("registerPasswordField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                label = "Confirma la contrasenya",
                required = true,
                isError = uiState.confirmPasswordError != null,
                errorText = uiState.confirmPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                modifier = Modifier.testTag("confirmPasswordField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.phoneNumber,
                onValueChange = { registerViewModel.onPhoneNumberChange(it) },
                label = "Telèfon",
                keyboardType = KeyboardType.Phone,
                modifier = Modifier.testTag("phoneField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.city,
                onValueChange = { registerViewModel.onCityChange(it) },
                label = "Ciutat",
                modifier = Modifier.testTag("cityField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.cycle,
                onValueChange = { registerViewModel.onCycleChange(it) },
                label = "Cicle formatiu",
                modifier = Modifier.testTag("cycleField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.bio,
                onValueChange = { registerViewModel.onBioChange(it) },
                label = "Biografia",
                singleLine = false,
                modifier = Modifier.testTag("bioField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.skillsText,
                onValueChange = { registerViewModel.onSkillsTextChange(it) },
                label = "Skills (separades per comes)",
                singleLine = false,
                modifier = Modifier.testTag("skillsField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.experienceLevel,
                onValueChange = { registerViewModel.onExperienceLevelChange(it) },
                label = "Nivell d'experiència",
                modifier = Modifier.testTag("experienceField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.languagesText,
                onValueChange = { registerViewModel.onLanguagesChange(it) },
                label = "Idiomes (separats per comes)",
                singleLine = false,
                modifier = Modifier.testTag("languagesField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.preferredRolesText,
                onValueChange = { registerViewModel.onPreferredRolesTextChange(it) },
                label = "Rols preferits (separats per comes)",
                singleLine = false,
                modifier = Modifier.testTag("preferredRolesField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.preferredLocation,
                onValueChange = { registerViewModel.onPreferredLocationChange(it) },
                label = "Ubicació preferida",
                modifier = Modifier.testTag("preferredLocationField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.availability,
                onValueChange = { registerViewModel.onAvailabilityChange(it) },
                label = "Disponibilitat",
                modifier = Modifier.testTag("availabilityField")
            )
        }

        item {
            RegisterTextField(
                value = uiState.portfolio,
                onValueChange = { registerViewModel.onPortfolioChange(it) },
                label = "Portfolio",
                modifier = Modifier.testTag("portfolioField")
            )
        }

        item {
            Button(
                onClick = { registerViewModel.register() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("registerButton")
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Registrar-se")
                }
            }
        }

        item {
            if (hasValidationErrors) {
                Text(
                    text = "Revisa els camps obligatoris marcats en vermell.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            uiState.serverError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .testTag("serverErrorText"),
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            Text(
                text = "Ja tens compte? Torna a login",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBackToLogin() }
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    required: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
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
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        supportingText = {
            if (!errorText.isNullOrBlank()) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
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