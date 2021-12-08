package ru.nifontbus.contactsevents.presentation.events.new_event.templates

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nifontbus.contactsevents.domain.data.Template
import ru.nifontbus.contactsevents.domain.use_cases.template.TemplatesUseCases
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(
    private val templatesUseCase: TemplatesUseCases
) : ViewModel() {

    private val _templates = mutableStateOf<List<Template>>(emptyList())
    val templates: State<List<Template>> = _templates

//    var newTemplateName = mutableStateOf("")

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

    init {
        getTemplates()
    }

    private fun getTemplates() = viewModelScope.launch {
        _templates.value = templatesUseCase.getTemplates()
    }

/*
    fun addTemplate() = viewModelScope.launch {
        when (val result = templatesUseCase.addTemplate(Template(newTemplateName.value))) {
            is Resource.Success -> {
                newTemplateName.value = ""
                sendMessage(result.message)
                getTemplates()
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }
*/

/*
    fun deleteTemplate(template: Template) = viewModelScope.launch {
        when (val result = templatesUseCase.deleteTemplate(template.id)) {
            is Resource.Success -> {
                sendMessage(result.message)
                getTemplates()
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }
*/


//    fun isEnabledSave(): Boolean = newTemplateName.value.isNotEmpty()

/*    private suspend fun sendMessage(msg: String) {
        _action.emit(msg)
    }*/
}