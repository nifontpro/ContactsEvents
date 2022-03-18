package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.service.MetadataService

class GetEvent(
    private val service: MetadataService
) {
    operator fun invoke() = service.event
}