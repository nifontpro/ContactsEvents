apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(Modules.core))
    "implementation" (project(Modules.eventsDomain))
    "implementation" (project(Modules.settingsDomain))
}