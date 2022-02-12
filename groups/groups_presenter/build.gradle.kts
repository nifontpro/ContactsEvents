apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(":core-ui"))
    "implementation"(project(":groups:groups_domain"))
    "implementation"(project(":settings:settings_domain"))
}