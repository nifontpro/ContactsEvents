package ru.nifontbus.settings_domain.model

sealed class MainEvent {
    object SyncAll: MainEvent()
}
