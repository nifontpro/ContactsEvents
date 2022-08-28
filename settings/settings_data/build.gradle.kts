apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(Modules.core))
    "implementation" (project(Modules.settingsDomain))
    "implementation" (project(Modules.groupsDomain))

    "implementation" (Datastore.datastore)
    "implementation" (Datastore.kotlinSerialization)
//    "implementation" (Datastore.immutable)
}
