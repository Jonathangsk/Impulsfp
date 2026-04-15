package com.impulsfp.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.SessionData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
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
    onOfferClick: (String) -> Unit,
    onApplicationsClick: () -> Unit,
    menuViewModel: MenuViewModel = viewModel(),
    offersViewModel: OffersViewModel = viewModel()
) {
    val user = SessionData.currentUser
    val displayName = user?.username ?: "Usuari"

    val offersUiState by offersViewModel.uiState.collectAsState()

    val menuTitle = when (user?.role) {
        "ADMIN" -> "Ofertes disponibles"
        "ALUMNE" -> "Ofertes de pràctiques"
        else -> "Ofertes"
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
            name = displayName,
            onHomeClick = { },
            onApplicationsClick = onApplicationsClick,
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
                    text = "Benvingut/da, $displayName",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Cerca
                OutlinedTextField(
                    value = offersUiState.searchQuery,
                    onValueChange = { offersViewModel.onSearchQueryChange(it) },
                    label = { Text("Cerca ofertes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("offersSearchField"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 📊 ESTATS
                when {
                    offersUiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    offersUiState.errorMessage != null -> {
                        Text(
                            text = offersUiState.errorMessage ?: "Error en carregar les ofertes",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    offersUiState.filteredOffers.isEmpty() -> {
                        Text(
                            text = "No s'han trobat ofertes.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(offersUiState.filteredOffers) { offer ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onOfferClick(offer.id) }
                                        .testTag("offerCard_${offer.id}"),
                                    shape = RoundedCornerShape(24.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(18.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = offer.title,
                                                    style = MaterialTheme.typography.titleLarge
                                                )

                                                Spacer(modifier = Modifier.height(6.dp))

                                                Text(
                                                    text = offer.company,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(50),
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                            ) {
                                                Text(
                                                    text = offer.modality,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Text(
                                            text = "📍 ${offer.location}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "📄 ${offer.contractType}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        if (!offer.salary.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "💰 ${offer.salary}",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Text(
                                            text = "Tecnologies",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            offer.requiredSkills.take(3).forEach { skill ->
                                                Surface(
                                                    shape = RoundedCornerShape(50),
                                                    color = MaterialTheme.colorScheme.secondaryContainer
                                                ) {
                                                    Text(
                                                        text = skill,
                                                        style = MaterialTheme.typography.labelMedium,
                                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}