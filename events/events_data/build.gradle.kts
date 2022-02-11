apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(":core"))
    "implementation" (project(":events:events_domain"))
}
