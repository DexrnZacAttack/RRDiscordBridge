plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}