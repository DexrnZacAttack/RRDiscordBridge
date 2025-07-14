plugins {
    java
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(8)
java.sourceCompatibility = JavaVersion.toVersion(8)
java.targetCompatibility = JavaVersion.toVersion(8)

repositories {
    mavenCentral()
    maven("https://nexus.scarsz.me/content/repositories/releases")
    maven("https://repo.md-5.net/content/groups/public")
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.0")
    implementation("club.minnced:discord-webhooks:0.8.4")
    implementation("net.dv8tion:JDA:5.5.1")
    implementation("me.scarsz.jdaappender:jda5:1.2.3")
    implementation("org.slf4j:slf4j-jdk14:2.0.17")
    implementation(project(":Bukkit 1.3.1-1.7.8"))
    compileOnly("org.bukkit:bukkit:$bukkit11McVersion-$bukkit11Version")
    implementation(project(":common"))
}

tasks.named("compileJava").configure {
    dependsOn(":Bukkit 1.3.1-1.7.8:shadowJar")
}