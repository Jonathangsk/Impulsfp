package com.impulsfp.mobile.ui

import androidx.lifecycle.ViewModel
import com.impulsfp.mobile.data.ApplicationUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ApplicationsViewModel : ViewModel() {

    private val _applications = MutableStateFlow(
        listOf(
            ApplicationUiModel(
                id = 1,
                offerTitle = "Desenvolupador/a Android Junior",
                companyName = "TechNova",
                location = "Barcelona",
                status = "Enviada",
                appliedAt = "10/04/2026"
            ),
            ApplicationUiModel(
                id = 2,
                offerTitle = "Programador/a Backend Java",
                companyName = "InnovaSoft",
                location = "Girona",
                status = "En revisió",
                appliedAt = "08/04/2026"
            ),
            ApplicationUiModel(
                id = 3,
                offerTitle = "Tècnic/a de Sistemes",
                companyName = "CloudBase",
                location = "Tarragona",
                status = "Acceptada",
                appliedAt = "04/04/2026"
            ),
            ApplicationUiModel(
                id = 4,
                offerTitle = "Desenvolupador/a Web Frontend",
                companyName = "DigitalMood",
                location = "Remot",
                status = "Rebutjada",
                appliedAt = "01/04/2026"
            )
        )
    )

    val applications: StateFlow<List<ApplicationUiModel>> = _applications.asStateFlow()
}