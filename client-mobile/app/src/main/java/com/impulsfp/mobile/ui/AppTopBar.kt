package com.impulsfp.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.impulsfp.mobile.R
import com.impulsfp.mobile.ui.theme.PrimaryBlue
import com.impulsfp.mobile.ui.theme.TextPrimary

/**
 * Barra superior comuna de l'aplicació.
 *
 * Es mostra a totes les pantalles internes excepte al login.
 * Inclou:
 * - logo clicable (torna a home)
 * - avatar (perfil)
 * - botó logout
 *
 * @param name Nom d'usuari per mostrar inicial
 * @param avatarId Identificador de l'avatar seleccionat
 * @param onHomeClick Acció quan es prem el logo
 * @param onProfileClick Acció quan es prem l'avatar
 * @param onLogoutClick Acció quan es prem logout
 */
@Composable
fun AppTopBar(
    name: String?,
    avatarId: Int,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val avatarColor = when (avatarId) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFF2196F3)
        3 -> PrimaryBlue
        else -> Color(0xFF9C27B0)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
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
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo ImpulsFP",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(56.dp)
                    .clickable { onHomeClick() }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(avatarColor)
                        .clickable { onProfileClick() }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = name?.take(1)?.uppercase() ?: "U",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

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