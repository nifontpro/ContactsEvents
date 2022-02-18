package ru.nifontbus.groups_presenter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.nifontbus.groups_domain.model.PersonsGroup
import ru.nifontbus.groups_domain.use_cases.GroupsUseCases
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupsUseCases: GroupsUseCases,
) : ViewModel() {

    val currentGroup = groupsUseCases.getCurrentGroup()

    val groups = groupsUseCases.getGroups()

//    var groupName = mutableStateOf("")

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

/*
    fun addGroup() = viewModelScope.launch {
        when (val result = groupsUseCases.addGroup(PersonsGroup(groupName.value))) {
            is Resource.Success -> {
                groupName.value = ""
                sendMessage(result.message)
            }
            is Resource.Error -> sendMessage(result.message)
        }
    }
*/

    fun setCurrentGroup(group: PersonsGroup) {
        if (currentGroup.value?.id != group.id) {
            groupsUseCases.setCurrentGroup(group)
        }
    }

/*    fun isEnabledSave(): Boolean = groupName.value.isNotEmpty()

    private suspend fun sendMessage(msg: String) {
        _action.emit(msg)
    }*/
}