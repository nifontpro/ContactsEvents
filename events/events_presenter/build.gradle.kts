apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(":core"))
    "implementation"(project(":core-ui"))
    "implementation"(project(":events:events_domain"))
    "implementation"(project(":persons:persons_domain"))
    "implementation"(project(":templates:templates_domain"))
    "implementation"(project(":settings:settings_domain"))

    "implementation" ("com.google.accompanist:accompanist-permissions:0.22.0-rc")


}