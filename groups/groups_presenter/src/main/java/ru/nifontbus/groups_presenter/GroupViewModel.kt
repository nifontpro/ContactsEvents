package ru.nifontbus.groups_presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.nifontbus.core_ui.component.BottomNavItem
import ru.nifontbus.groups_domain.model.PersonsGroup
import ru.nifontbus.groups_domain.use_cases.GroupsUseCases
import ru.nifontbus.settings_domain.model.MainEvent
import ru.nifontbus.settings_domain.use_cases.MetadataUseCases
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupsUseCases: GroupsUseCases,
    private val metadataUseCases: MetadataUseCases,
) : ViewModel() {

    val currentGroup = groupsUseCases.getCurrentGroup()

    val groups = groupsUseCases.getGroups()

    private val _action = MutableSharedFlow<String>()
    val action: SharedFlow<String> = _action.asSharedFlow()

    init {
        syncGroupsSubscribe()
        groupsCountSubscribe()
    }

    private fun syncGroupsSubscribe() = viewModelScope.launch {
        metadataUseCases.getEvent().collectLatest { event ->
            if (event is MainEvent.SyncAll) {
                groupsUseCases.syncGroups()
            }
        }
    }

    private fun groupsCountSubscribe() = viewModelScope.launch {
        groups.collectLatest {
            BottomNavItem.GroupItem.badgeCount.value = it.size
        }
    }

    fun setCurrentGroup(group: PersonsGroup) {
        if (currentGroup.value?.id != group.id) {
            groupsUseCases.setCurrentGroup(group)
        }
    }
}