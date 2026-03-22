package com.impulsfp.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.impulsfp.mobile.navigation.AppNavigation
import com.impulsfp.mobile.ui.theme.ImpulsFPTheme

/**
 * Activitat principal de l'aplicació.
 *
 * Inicialitza la interfície gràfica utilitzant Jetpack Compose
 * i estableix la navegació principal mitjançant [AppNavigation].
 *
 * @author abenitez
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ImpulsFPTheme {
                AppNavigation()
            }
        }
    }
}
