package ru.nifontbus.templates_data.repository

import android.content.Context
import ru.nifontbus.events_domain.model.EventType
import ru.nifontbus.templates_data.R
import ru.nifontbus.templates_domain.model.Template
import ru.nifontbus.templates_domain.repository.TemplatesRepository

class TemplatesRepositoryImpl(
    private val context: Context
) : TemplatesRepository {
    override fun getTemplates(useReposeFeatures: Boolean): List<Template> {
        val list = mutableListOf<Template>()
        list.add(Template(0, EventType.CUSTOM))
        list.add(Template(1, EventType.BIRTHDAY))
        list.add(Template(2, EventType.OTHER))
        list.add(Template(3, EventType.ANNIVERSARY))
        list.add(Template(4, EventType.CUSTOM, context.getString(R.string.sAngelDay)))
        list.add(Template(5, EventType.CUSTOM, context.getString(R.string.sWeddingDay)))
        if (useReposeFeatures) list.add(Template(6, EventType.NEW_LIFE_DAY))
        return list
    }
}