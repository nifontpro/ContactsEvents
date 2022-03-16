apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreUi))
    "implementation" (project(Modules.settingsDomain))
    "implementation" (project(Modules.workerDomain))
}