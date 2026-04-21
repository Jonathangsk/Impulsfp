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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.impulsfp.mobile.R
import com.impulsfp.mobile.ui.theme.TextPrimary

/**
 * Barra superior comuna de l'aplicació.
 *
 * Aquest component mostra el logotip de l'aplicació i els accessos
 * principals de navegació mitjançant icones:
 *
 * - Inici / Ofertes
 * - Candidatures
 * - Perfil
 * - Tancar sessió
 *
 * El logotip també funciona com a acció per tornar a la pantalla
 * principal.
 *
 * Aquesta barra es reutilitza a diferents pantalles per mantenir
 * una navegació coherent i una experiència d'usuari homogènia.
 *
 * També incorpora identificadors de test (`testTag`) per facilitar
 * els tests d'integració i navegació amb Jetpack Compose UI Test.
 *
 * @param name Nom de l'usuari. Actualment no es mostra visualment,
 * però es manté disponible per a futures ampliacions.
 * @param onHomeClick Funció executada en prémer inici
 * @param onApplicationsClick Funció executada en prémer candidatures
 * @param onProfileClick Funció executada en prémer perfil
 * @param onLogoutClick Funció executada en prémer tancar sessió
 *
 * @author abenitez
 */
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

            /**
             * Logotip principal clickable.
             *
             * Permet tornar a la pantalla principal.
             *
             * També disposa del testTag "homeLogoButton"
             * per poder ser localitzat en tests automatitzats.
             */
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo ImpulsFP",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(56.dp)
                    .testTag("homeLogoButton")
                    .clickable { onHomeClick() }
            )

            /**
             * Grup d'icones de navegació principal.
             */
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                /**
                 * Accés a la pantalla principal d'ofertes.
                 *
                 * testTag: "homeButton"
                 */
                IconButton(
                    onClick = onHomeClick,
                    modifier = Modifier.testTag("homeButton")
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Ofertes",
                        tint = TextPrimary
                    )
                }

                /**
                 * Accés a la pantalla de candidatures enviades.
                 *
                 * testTag: "applicationsButton"
                 */
                IconButton(
                    onClick = onApplicationsClick,
                    modifier = Modifier.testTag("applicationsButton")
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Les meves candidatures",
                        tint = TextPrimary
                    )
                }

                /**
                 * Accés a la pantalla del perfil d'usuari.
                 *
                 * testTag: "profileButton"
                 */
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.testTag("profileButton")
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = TextPrimary
                    )
                }

                /**
                 * Tancament de sessió de l'usuari actual.
                 *
                 * testTag: "logoutButton"
                 */
                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.testTag("logoutButton")
                ) {
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