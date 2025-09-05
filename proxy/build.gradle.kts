plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
}

dependencies {
    implementation(project(":common"))
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    jar {
        enabled = false
    }

    shadowJar {
        destinationDirectory.set(layout.projectDirectory.dir("../output"))
        archiveFileName.set("${rootProject.name}Proxy.jar")
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }
}