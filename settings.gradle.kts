pluginManagement {
    repositories {
        // maven("https://maven.neuralnexus.dev/mirror")
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.wagyourtail.xyz/releases")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}
rootProject.name = "RRDiscordBridge"

// apparently the projects are actually handled here
//include(":common")
//include(":fabric")
//include(":poseidon")
//include(":bukkit10")
//include(":bukkit11")
//include(":bukkit13")
//include(":bukkit17")
//project(":fabric").projectDir = file("fabric")
//project(":poseidon").projectDir = file("bukkit/poseidon")
//project(":bukkit10").projectDir = file("bukkit/bukkit10")
//project(":bukkit11").projectDir = file("bukkit/bukkit11")
//project(":bukkit13").projectDir = file("bukkit/bukkit13")
//project(":bukkit17").projectDir = file("bukkit/bukkit17")
//project(":fabric").name = "Fabric 1.19.2+"
//project(":poseidon").name = "Project Poseidon CraftBukkit"
//project(":bukkit10").name = "Bukkit b1.4-r1.0.1"
//project(":bukkit11").name = "Bukkit 1.1-1.2.5"
//project(":bukkit13").name = "Bukkit 1.3.1-1.7.8"
//project(":bukkit17").name = "Bukkit 1.7.9+"
