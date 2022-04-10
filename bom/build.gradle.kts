plugins {
    `java-platform`
    `maven-publish`
}

javaPlatform.allowDependencies()

dependencies {
    constraints {
        rootProject.subprojects
            .filter { it.name != project.name }
            .sortedBy { it.name }
            .forEach {
                api(it)
            }
    }
}

publishing {
    publications {
        create<MavenPublication>("bom") {
            from(components.getByName("javaPlatform"))
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
        }
    }
    repositories {
        mavenLocal()
        maven {
            name = "InternalRepo"
            url = uri("https://m2.open-edgn.cn/repository/maven-snapshots/")
            credentials {
                username = (project.findProperty("edgn.m2.user") ?: System.getenv("USERNAME")).toString()
                password = (project.findProperty("edgn.m2.key") ?: System.getenv("TOKEN")).toString()
            }
        }
    }
}
