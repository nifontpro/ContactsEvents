package ru.nifontbus.templates_domain.repository

import ru.nifontbus.templates_domain.model.Template

interface TemplatesRepository {
    fun getTemplates(useReposeFeatures: Boolean): List<Template>
}