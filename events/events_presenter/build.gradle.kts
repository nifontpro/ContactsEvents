apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.eventsDomain))
    "implementation"(project(Modules.personsDomain))
    "implementation" (project(Modules.groupsDomain))
    "implementation"(project(Modules.templatesDomain))
    "implementation"(project(Modules.settingsDomain))

    "implementation" ("com.google.accompanist:accompanist-permissions:0.22.0-rc")



}