package ru.nifontbus.events_domain.use_cases

import ru.nifontbus.events_domain.repository.EventsRepository

class SilentSync(
    private val eventsRepository: EventsRepository
) {
    operator fun invoke() = eventsRepository.silentSync()
}