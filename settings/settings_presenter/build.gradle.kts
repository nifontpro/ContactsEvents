apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(":core-ui"))
    "implementation" (project(":settings:settings_domain"))
}