package ru.nifontbus.settings_domain.use_cases

import ru.nifontbus.settings_domain.service.MetadataService


// Отправить сообщение для рассылки
class SendMessage(
    private val service: MetadataService
) {
    suspend operator fun invoke(message: String) = service.sendMessage(message)
}