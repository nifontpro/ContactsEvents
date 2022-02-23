apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(":core"))
    "implementation"(project(":persons:persons_domain"))
    "implementation"(Compose.uiGraphics)
}
