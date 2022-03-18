apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreUi))
    "implementation"(project(Modules.groupsDomain))
    "implementation"(project(Modules.settingsDomain))
}