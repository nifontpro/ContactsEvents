apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.templatesDomain))
    "implementation"(project(Modules.eventsDomain))
}