package ru.nifontbus.templates_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.nifontbus.settings_domain.repository.SettingsRepository
import ru.nifontbus.templates_domain.repository.TemplatesRepository
import ru.nifontbus.templates_domain.use_cases.GetTemplates
import ru.nifontbus.templates_domain.use_cases.TemplatesUseCases
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object TemplatesDomainModule {

    @Provides
    @ViewModelScoped
    fun provideTemplatesUseCases(
        templatesRepository: TemplatesRepository,
        settingsRepository: SettingsRepository
    ): TemplatesUseCases {
        return TemplatesUseCases(
            getTemplates = GetTemplates(
                repository = templatesRepository,
                settingsRepository = settingsRepository
            ),
        )
    }

}