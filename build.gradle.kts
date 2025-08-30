plugins {
    kotlin("jvm") version "2.2.20-RC" apply false
}

allprojects {
    group = "com.ruineko"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}