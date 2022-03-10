apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(Modules.workerDomain))
    "implementation" (project(Modules.eventsDomain))
}
