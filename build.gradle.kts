// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
//        classpath(Build.androidBuildTools)
		classpath("com.android.tools.build:gradle:7.2.2") // For update

		classpath(Build.hiltAndroidGradlePlugin)
		classpath(Build.kotlinGradlePlugin)
		classpath("org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}")

		// NOTE: Do not place your application dependencies here; they belong
		// in the individual module build.gradle files
	}
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}