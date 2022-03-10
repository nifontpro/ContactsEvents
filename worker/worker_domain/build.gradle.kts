apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation" (project(Modules.eventsDomain))
    "kapt" (WorkManager.hiltCompiler) // Для внедрения зависимостей (репозитория) в Worker
    "implementation" (WorkManager.work)
    "implementation" (WorkManager.hiltWork)
}
