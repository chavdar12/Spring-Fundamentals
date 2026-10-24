plugins {
    // Downloads the JDK requested by the toolchain if it isn't installed locally
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Spring-Fundamentals"

include("Cookies-Demo", "Coudinary-Demo", "Mobilele", "Workshop")
