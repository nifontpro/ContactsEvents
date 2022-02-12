apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(":core"))
    "implementation" (project(":templates:templates_domain"))
    "implementation" (project(":events:events_domain"))
}
