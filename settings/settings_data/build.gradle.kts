apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(Modules.core))
    "implementation" (project(Modules.settingsDomain))
    "implementation" (project(Modules.groupsDomain))

    "implementation" ("androidx.datastore:datastore:1.0.0")
    "implementation" ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
//    "implementation" ("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
}
