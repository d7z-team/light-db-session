group = rootProject.property("group")!!
version = rootProject.property("version")!!

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://m2.open-edgn.cn/repository/maven-snapshots/")
        maven("https://m2.open-edgn.cn/repository/maven-releases/")
        maven { url = project.uri("https://jitpack.io") }
    }
    dependencies {
        val kotlinVersion = rootProject.property("version.kotlin")!!
        val ktlintVersion = rootProject.property("version.kt-lint")!!
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:$ktlintVersion")
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://m2.open-edgn.cn/repository/maven-snapshots/")
        maven("https://m2.open-edgn.cn/repository/maven-releases/")
        maven { url = project.uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    for (childProject in childProjects.values) {
        delete(childProject.buildDir)
    }
}
