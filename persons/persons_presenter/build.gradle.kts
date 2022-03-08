apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.personsDomain))
    "implementation"(project(Modules.settingsDomain))
    "implementation"(project(Modules.eventsPresenter))
    "implementation"(project(Modules.eventsDomain))
    "implementation" (project(Modules.groupsDomain))

    // Toolbar
    "implementation"("me.onebone:toolbar-compose:2.3.1")
}