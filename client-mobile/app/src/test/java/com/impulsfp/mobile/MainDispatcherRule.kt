package com.impulsfp.mobile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Rule de testing per substituir temporalment el dispatcher principal
 * de Kotlin Coroutines durant l'execució dels tests.
 *
 * Aquesta classe permet controlar les coroutines que utilitzen
 * [Dispatchers.Main], especialment en ViewModels i components
 * que executen tasques asíncrones.
 *
 * Durant cada test:
 * - abans d'iniciar-se, es reemplaça Main pel dispatcher de prova
 * - en finalitzar, es restaura el dispatcher original
 *
 * Això facilita proves deterministes i compatibles amb
 * `runTest`, `advanceUntilIdle()` i altres utilitats de testing.
 *
 * @param dispatcher Dispatcher utilitzat durant el test.
 * Per defecte s'utilitza [StandardTestDispatcher].
 *
 * @author abenitez
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    /**
     * S'executa abans de cada test.
     *
     * Substitueix [Dispatchers.Main] pel dispatcher de proves.
     */
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    /**
     * S'executa en finalitzar cada test.
     *
     * Restaura el dispatcher principal original.
     */
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}