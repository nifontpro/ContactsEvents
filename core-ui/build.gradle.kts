apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    // Permissions
    "implementation" ("com.google.accompanist:accompanist-permissions:0.22.0-rc")
}