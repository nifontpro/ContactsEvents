apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(":core"))
    "implementation"(project(":core-ui"))
    "implementation"(project(":persons:persons_domain"))
    "implementation"(project(":settings:settings_domain"))
    "implementation"(project(":events:events_presenter"))
    "implementation"(project(":events:events_domain"))
    "implementation" (project(":groups:groups_domain"))

    // Toolbar
    "implementation"("me.onebone:toolbar-compose:2.3.0")



}