apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(Modules.core))
    "implementation" (project(Modules.groupsDomain))

    "implementation" ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}