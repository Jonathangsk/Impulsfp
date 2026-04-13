package com.impulsfp.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.impulsfp.mobile.R
import com.impulsfp.mobile.ui.theme.TextPrimary

@Composable
fun AppTopBar(
    name: String?,
    onHomeClick: () -> Unit,
    onApplicationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔵 LOGO (segueix sent clickable)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo ImpulsFP",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(56.dp)
                    .clickable { onHomeClick() }
            )

            // 🔵 ICONES
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🏠 HOME
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Ofertes",
                        tint = TextPrimary
                    )
                }

                // 💼 CANDIDATURES
                IconButton(onClick = onApplicationsClick) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Les meves candidatures",
                        tint = TextPrimary
                    )
                }

                // 👤 PERFIL
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = TextPrimary
                    )
                }

                // 🚪 LOGOUT
                IconButton(onClick = onLogoutClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Tancar sessió",
                        tint = TextPrimary
                    )
                }
            }
        }
    }
}