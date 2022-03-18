apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    // Permissions
    "implementation" (Compose.permission)
}