apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(":core"))
    "implementation"(project(":persons:persons_domain"))
    "implementation"(project(":settings:settings_domain"))
    "implementation"(Compose.uiGraphics)
}
