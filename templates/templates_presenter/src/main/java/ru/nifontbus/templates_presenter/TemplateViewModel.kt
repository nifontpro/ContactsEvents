package ru.nifontbus.templates_presenter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nifontbus.templates_domain.use_cases.TemplatesUseCases
import ru.nifontbus.templates_domain.model.Template
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(
    private val templatesUseCase: TemplatesUseCases
) : ViewModel() {

    private val _templates = mutableStateOf<List<Template>>(emptyList())
    val templates: State<List<Template>> = _templates

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

    init {
        getTemplates()
    }

    private fun getTemplates() = viewModelScope.launch {
        _templates.value = templatesUseCase.getTemplates()
    }
}