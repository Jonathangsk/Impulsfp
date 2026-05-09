package com.impulsfp.mobile.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.impulsfp.mobile.data.Offer
import kotlinx.coroutines.delay
/**
 * Pantalla encarregada de mostrar i gestionar la prova tècnica
 * associada a una oferta.
 *
 * Aquesta pantalla presenta la pregunta de la prova, el possible
 * fragment de codi i les opcions de resposta disponibles.
 *
 * També controla el temps màxim per completar la prova mitjançant
 * un compte enrere i registra automàticament la prova com a no
 * completada si el temps s'esgota.
 *
 * En finalitzar correctament, desa la resposta seleccionada
 * al ViewModel d'ofertes i retorna a la pantalla anterior.
 *
 * @author abenitez
 */
@Composable
fun TechnicalTestScreen(
    offer: Offer,
    offersViewModel: OffersViewModel,
    onBackClick: () -> Unit,
    onTestCompleted: () -> Unit
) {
    val context = LocalContext.current

    var selectedAnswer by remember { mutableStateOf("") }
    var remainingSeconds by remember { mutableStateOf(120) }
    var timeExpired by remember { mutableStateOf(false) }

    val optionsList = offer.options

    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }

        timeExpired = true

        offersViewModel.markTechnicalTestAsCompleted(
            offerId = offer.id,
            answer = "TIMEOUT"
        )

        Toast.makeText(
            context,
            "Temps esgotat. La prova s'ha enviat automàticament.",
            Toast.LENGTH_SHORT
        ).show()

        onTestCompleted()
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .testTag("technicalTestScreen")
    ) {
        Text(
            text = "Prova tècnica",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Temps restant: %02d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.titleMedium,
            color = if (remainingSeconds <= 10) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tipus: ${offer.testType}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = offer.testQuestion ?: "",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        offer.codeSnippet?.let { code ->
            Text(
                text = code,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        optionsList.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedAnswer == option,
                        enabled = !timeExpired,
                        onClick = { selectedAnswer = option }
                    )
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedAnswer == option,
                    onClick = { selectedAnswer = option },
                    enabled = !timeExpired
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = option)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedAnswer.isNotBlank() && !timeExpired) {
                    offersViewModel.markTechnicalTestAsCompleted(
                        offerId = offer.id,
                        answer = selectedAnswer
                    )
                    onTestCompleted()
                }
            },
            enabled = selectedAnswer.isNotBlank() && !timeExpired,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalitzar prova")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            enabled = !timeExpired,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tornar")
        }
    }
}