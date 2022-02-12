apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(":core"))
    "implementation" (project(":groups:groups_domain"))

    "implementation"(Compose.uiGraphics)
}