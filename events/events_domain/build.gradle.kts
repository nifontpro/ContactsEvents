apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(":core"))
    "implementation" (project(":settings:settings_domain"))
}