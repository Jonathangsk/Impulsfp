package com.impulsfp.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.Offer
import com.impulsfp.mobile.data.SessionData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
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

    var cityExpanded by remember { mutableStateOf(false) }
    var modalityExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = offersUiState.searchQuery,
                    onValueChange = { offersViewModel.onSearchQueryChange(it) },
                    label = { Text("Cerca ofertes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("offersSearchField"),
                    singleLine = true
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExposedDropdownMenuBox(
                        expanded = cityExpanded,
                        onExpandedChange = { cityExpanded = !cityExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = offersUiState.selectedCity.ifBlank { "Totes" },
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            label = { Text("Ciutat") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("offersCityFilterDropdown")
                        )

                        ExposedDropdownMenu(
                            expanded = cityExpanded,
                            onDismissRequest = { cityExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Totes") },
                                onClick = {
                                    offersViewModel.onCityFilterChange("")
                                    cityExpanded = false
                                }
                            )

                            offersUiState.availableCities.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        offersViewModel.onCityFilterChange(city)
                                        cityExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = modalityExpanded,
                        onExpandedChange = { modalityExpanded = !modalityExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = if (offersUiState.selectedModality.isBlank()) {
                                "Totes"
                            } else {
                                formatModality(offersUiState.selectedModality)
                            },
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            label = { Text("Modalitat") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = modalityExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("offersModalityFilterDropdown")
                        )

                        ExposedDropdownMenu(
                            expanded = modalityExpanded,
                            onDismissRequest = { modalityExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Totes") },
                                onClick = {
                                    offersViewModel.onModalityFilterChange("")
                                    modalityExpanded = false
                                }
                            )

                            offersUiState.availableModalities.forEach { modality ->
                                DropdownMenuItem(
                                    text = { Text(formatModality(modality)) },
                                    onClick = {
                                        offersViewModel.onModalityFilterChange(modality)
                                        modalityExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Netejar filtres",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { offersViewModel.clearFilters() }
                )
            }

            when {
                offersUiState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                offersUiState.errorMessage != null -> {
                    item {
                        Text(
                            text = offersUiState.errorMessage ?: "Error en carregar les ofertes",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                offersUiState.filteredOffers.isEmpty() -> {
                    item {
                        Text(
                            text = "No s'han trobat ofertes.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    items(offersUiState.filteredOffers) { offer ->
                        OfferCard(
                            offer = offer,
                            onOfferClick = onOfferClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferCard(
    offer: Offer,
    onOfferClick: (String) -> Unit
) {
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
                        text = formatModality(offer.modality),
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

            Text(
                text = "🎓 ${offer.cycle}",
                style = MaterialTheme.typography.bodyMedium
            )

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

private fun formatModality(modality: String): String {
    return when (modality.uppercase()) {
        "REMOTE" -> "Remot"
        "HYBRID" -> "Híbrid"
        "ONSITE" -> "Presencial"
        else -> modality
    }
}