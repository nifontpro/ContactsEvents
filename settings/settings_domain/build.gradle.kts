apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(Modules.core))
    "implementation" (project(Modules.groupsDomain))

    "implementation" (Datastore.kotlinSerialization)
}