apply {
    from("$rootDir/compose-module.gradle")
}

dependencies {
    "implementation"(project(":core"))
    "implementation"(project(":core-ui"))
    "implementation"(project(":persons:persons_domain"))
}