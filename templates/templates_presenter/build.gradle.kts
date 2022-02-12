apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(":core-ui"))
    "implementation"(project(":templates:templates_domain"))
    "implementation"(project(":events:events_domain"))
}