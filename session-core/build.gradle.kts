import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    `maven-publish`
}
java.sourceCompatibility = JavaVersion.VERSION_11

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileKotlin.destinationDirectory.set(compileJava.destinationDirectory.get())

java {
    modularity.inferModulePath.set(true)
}

val junitJupiterVersion = rootProject.property("version.junit.jupiter")!!
val junitLauncherVersion = rootProject.property("version.junit.launcher")!!
val objectFormatVersion = rootProject.property("version.object-format")!!
val lightDBVersion = rootProject.property("version.light-db")!!
dependencies {
    implementation(platform("com.github.d7z-team.light-db:bom:$lightDBVersion"))
    implementation(platform("com.github.d7z-team.object-format:bom:$objectFormatVersion"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation("com.github.d7z-team.light-db:db-api")
    api("com.github.d7z-team.object-format:format-core")
    testImplementation("com.github.d7z-team.object-format:format-extra-gson")
    testImplementation("com.github.d7z-team.light-db:db-memory")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testImplementation("org.junit.platform:junit-platform-launcher:$junitLauncherVersion")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = (parent ?: rootProject).group.toString()
            version = (parent ?: rootProject).version.toString()
            artifactId = project.name
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
    repositories {
        mavenLocal()
    }
}
