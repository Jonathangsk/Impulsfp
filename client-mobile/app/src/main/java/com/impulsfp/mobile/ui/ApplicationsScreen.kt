package com.impulsfp.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.impulsfp.mobile.data.ApplicationUiModel

@Composable
fun ApplicationsScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit,
    menuViewModel: MenuViewModel = viewModel(),
    applicationsViewModel: ApplicationsViewModel = viewModel()
) {
    val applications by applicationsViewModel.applications.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppTopBar(
            name = "Candidatures",
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
                .padding(20.dp)
        ) {
            Text(
                text = "Les meves candidatures",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${applications.size} candidatures",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (applications.isEmpty()) {
                Text(
                    text = "Encara no has enviat cap candidatura.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(applications) { application ->
                        ApplicationCard(application = application)
                    }
                }
            }
        }
    }
}

@Composable
private fun ApplicationCard(application: ApplicationUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = application.offerTitle,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = application.companyName,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = application.location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusChip(application.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Inscripció enviada: ${application.appliedAt}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val backgroundColor = when (status) {
        "Enviada" -> Color(0xFFE3F2FD)
        "En revisió" -> Color(0xFFFFF3E0)
        "Acceptada" -> Color(0xFFE8F5E9)
        "Rebutjada" -> Color(0xFFFFEBEE)
        else -> MaterialTheme.colorScheme.secondaryContainer
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}