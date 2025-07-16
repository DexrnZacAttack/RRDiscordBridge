import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.java
import xyz.wagyourtail.unimined.api.minecraft.task.RemapJarTask
import java.time.Instant

// NOTE: NOT FINISHED
// When I originally set up the script, I didn't understand how to use it, and accidentally just started making separate build scripts everywhere lmao
// I have attempted to reverse this, and properly match the original template's structure.
// This means all other build scripts for each platform are not used.
// This was committed for debugging, currently Bukkit builds do not work properly to my knowledge.
// The current issue is that Fabric builds do not remap properly, it almost looks like the symbols get re-obfuscated instead
// This leads to NoSuchMethodException whenever one of these methods are called, ex:
/* [21:31:23] [JDA MainWS-ReadThread/ERROR]: One of the EventListeners had an uncaught exception
 * java.lang.NoSuchMethodError: 'net.minecraft.class_3324 net.minecraft.server.MinecraftServer.ag()'
 *      at knot/io.github.dexrnzacattack.rrdiscordbridge.impls.FabricServer.broadcastMessage(FabricServer.java:23) ~[RRDiscordBridge-2.1.0-shaded.jar:?]
*/


plugins {
    id("java")
    id("maven-publish")
    id("idea")
    id("eclipse")
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
    alias(libs.plugins.unimined)
}

tasks.named("build").configure {
    dependsOn("shadowJar")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
java.sourceCompatibility = JavaVersion.toVersion(javaVersion)
java.targetCompatibility = JavaVersion.toVersion(javaVersion)

spotless {
    format("misc") {
        target("*.gradle.kts", ".gitattributes", ".gitignore")
        trimTrailingWhitespace()
        leadingTabsToSpaces()
        endWithNewline()
    }
    java {
        target("src/**/*.java", "src/**/*.java.peb")
        toggleOffOn()
        importOrder()
        removeUnusedImports()
        cleanthat()
        googleJavaFormat("1.24.0")
            .aosp()
            .formatJavadoc(true)
            .reorderImports(true)
        formatAnnotations()
        trimTrailingWhitespace()
        leadingTabsToSpaces()
        endWithNewline()
//        licenseHeader("""/**
// * Copyright (c) 2025 $author
// * This project is Licensed under <a href="$sourceUrl/blob/main/LICENSE">$license</a>
// */""")
    }
}

val common: SourceSet by sourceSets.creating {
    blossom.javaSources {
        property("mod_id", modId)
        property("mod_name", modName)
        property("version", version.toString())
        property("license", license)
        property("author", author)
        property("description", description)
        property("homepage_url", homepageUrl)
    }
}

val fabric: SourceSet by sourceSets.creating
val neoforge: SourceSet by sourceSets.creating
val paper: SourceSet by sourceSets.creating
val poseidon: SourceSet by sourceSets.creating
val bukkit10: SourceSet by sourceSets.creating
val bukkit11: SourceSet by sourceSets.creating
val bukkit13: SourceSet by sourceSets.creating
val bukkit17: SourceSet by sourceSets.creating

listOf(bukkit13).forEach {
    listOf(common).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkit10).forEach {
    listOf(common, bukkit11, bukkit13).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(poseidon).forEach {
    listOf(common, bukkit11, bukkit13).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkit11).forEach {
    listOf(common, bukkit13).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkit17).forEach {
    listOf(common, bukkit13).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

val mainCompileOnly: Configuration by configurations.creating
configurations.compileOnly.get().extendsFrom(mainCompileOnly)
val commonCompileOnly: Configuration by configurations.getting
val fabricCompileOnly: Configuration by configurations.getting
val neoforgeCompileOnly: Configuration by configurations.getting
val paperCompileOnly: Configuration by configurations.getting {
    extendsFrom(commonCompileOnly)
}
val poseidonCompileOnly: Configuration by configurations.getting
val bukkit10CompileOnly: Configuration by configurations.getting
val bukkit11CompileOnly: Configuration by configurations.getting
val bukkit13CompileOnly: Configuration by configurations.getting
val bukkit17CompileOnly: Configuration by configurations.getting

listOf(fabricCompileOnly, neoforgeCompileOnly,
    paperCompileOnly, bukkit10CompileOnly, bukkit11CompileOnly, bukkit13CompileOnly, bukkit17CompileOnly, poseidonCompileOnly).forEach {
    it.extendsFrom(commonCompileOnly)
}
val modImplementation: Configuration by configurations.creating
val fabricModImplementation: Configuration by configurations.creating {
    extendsFrom(modImplementation)
}
val commonImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<RemapJarTask> {
    mixinRemap {
        enableBaseMixin()
        disableRefmap()
    }
}

repositories {
    // maven("https://maven.neuralnexus.dev/mirror")
    mavenCentral()
    unimined.fabricMaven()
    unimined.minecraftForgeMaven()
    unimined.neoForgedMaven()
    unimined.parchmentMaven()
    unimined.spongeMaven()
    maven("https://repo.md-5.net/content/groups/public") // Bukkit
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://nexus.scarsz.me/content/repositories/releases")
    maven("https://repository.johnymuffin.com/repository/maven-public/") // PP
}

unimined.minecraft {
    combineWith(common);
    version(minecraftVersion)
    mappings {
        parchment(parchmentMinecraft, parchmentVersion)
        mojmap()
        devFallbackNamespace("official")
    }

    defaultRemapJar = false
}

unimined.minecraft(fabric) {
    combineWith(sourceSets.main.get())
    fabric {
        loader(fabricLoaderVersion)
        accessWidener(project.projectDir.resolve("src/fabric/resources/rrdiscordbridge.accesswidener"))
    }

    defaultRemapJar = true
}

tasks.register<ShadowJar>("relocateFabricJar") {
    dependsOn("remapFabricJar")
    from(zipTree(tasks.getByName<Jar>("remapFabricJar").archiveFile.get().asFile))
    archiveClassifier.set("fabric-relocated")
    relocate("io.github.dexrnzacattack.rrdiscordbridge.mixin.vanilla", "io.github.dexrnzacattack.rrdiscordbridge.mixin.y_intmdry")
    relocate("io.github.dexrnzacattack.rrdiscordbridge.vanilla", "io.github.dexrnzacattack.rrdiscordbridge.y_intmdry")
}

unimined.minecraft(neoforge) {
    combineWith(sourceSets.main.get())
    neoForge {
        loader(neoForgeVersion)
    }
    defaultRemapJar = true
}

unimined.minecraft(paper) {
    combineWith(sourceSets.main.get())
    combineWith(bukkit17)
    accessTransformer {
        // https://github.com/PaperMC/Paper/blob/main/build-data/paper.at
//        accessTransformer("${rootProject.projectDir}/src/paper/paper.at")
    }
    defaultRemapJar = true
}

tasks.register<Jar>("bukkit11Jar") {
    group = "build"
    archiveBaseName.set("${modName}-${bukkit11.name.replace(" ", "_")}")
    from(bukkit11.output)
}

tasks.register<Jar>("bukkit10Jar") {
    group = "build"
    archiveBaseName.set("${modName}-${bukkit10.name.replace(" ", "_")}")
    from(bukkit10.output)
}

tasks.register<Jar>("bukkit13Jar") {
    group = "build"
    archiveBaseName.set("${modName}-${bukkit13.name.replace(" ", "_")}")
    from(bukkit13.output)
}

tasks.register<Jar>("poseidonJar") {
    group = "build"
    archiveBaseName.set("${modName}-${poseidon.name.replace(" ", "_")}")
    from(poseidon.output)
}

tasks.register<Jar>("bukkit17Jar") {
    group = "build"
    archiveBaseName.set("${modName}-${bukkit17.name.replace(" ", "_")}")
    from(bukkit17.output)
}

dependencies {
    commonCompileOnly(libs.annotations)
    commonCompileOnly(libs.mixin)
    listOf("api-base", "command-api-v2", "lifecycle-events-v1", "networking-api-v1", "entity-events-v1", "message-api-v1").forEach {
        fabricModImplementation(fabricApi.fabricModule("fabric-$it", fabricVersion))
    }
    paperCompileOnly("io.papermc.paper:paper-api:$minecraftVersion-$paperVersion")
    paperCompileOnly(libs.ignite.api)
    bukkit10CompileOnly("org.bukkit:bukkit:$bukkit10McVersion-$bukkit10Version")
    bukkit11CompileOnly("org.bukkit:bukkit:$bukkit11McVersion-$bukkit11Version")
    bukkit13CompileOnly("org.bukkit:bukkit:$bukkit13McVersion-$bukkit13Version")
    bukkit17CompileOnly("org.bukkit:bukkit:$bukkit17McVersion-$bukkit17Version")
    poseidonCompileOnly("com.legacyminecraft.poseidon:poseidon-craftbukkit:${poseidonVersion}")

    implementation("com.google.code.gson:gson:2.13.0")
    implementation("club.minnced:discord-webhooks:0.8.4")
    implementation("net.dv8tion:JDA:5.5.1")
    implementation("me.scarsz.jdaappender:jda5:1.2.3")
    implementation("org.slf4j:slf4j-jdk14:2.0.17")
}

tasks.withType<ProcessResources> {
    filesMatching(listOf(
        "bungee.yml",
        "fabric.mod.json",
        "pack.mcmeta",
        "META-INF/mods.toml",
        "META-INF/neoforge.mods.toml",
        "plugin.yml",
        "paper-plugin.yml",
        "ignite.mod.json",
        "META-INF/sponge_plugins.json",
        "velocity-plugin.json"
    )) {
        expand(project.properties)
    }
}

tasks.shadowJar {
    dependsOn("relocateFabricJar")
    from(
        zipTree(tasks.getByName<Jar>("relocateFabricJar").archiveFile.get().asFile),
//        neoforge.output,
//        paper.output,
//        bukkit13.output,
//        bukkit10.output,
//        bukkit11.output,
//        bukkit17.output
    )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to modName,
                "Specification-Version" to version,
                "Specification-Vendor" to "SomeVendor",
                "Implementation-Version" to version,
                "Implementation-Vendor" to "SomeVendor",
                "Implementation-Timestamp" to Instant.now().toString(),
                "FMLCorePluginContainsFMLMod" to "true",
                "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                "MixinConfigs" to "$modId.mixins.vanilla.json,$modId.mixins.forge.json"
            )
        )
    }
    from(listOf("README.md", "LICENSE")) {
        into("META-INF")
    }

    archiveClassifier = "shaded"
    exclude("META-INF")

    exclude("natives/**")
    exclude("com/sun/jna/**")
    exclude("com/google/crypto/tink/**")
    exclude("com/google/protobuf/**")
    exclude("google/protobuf/**")
    exclude("club/minnced/opus/util/*")
    exclude("tomp2p/opuswrapper/*")
    exclude("org/bukkit/craftbukkit/**") // stub for old craftbukkit, would shove in it's build script if I knew how to append to shadowJar config without outright copying and pasting this block to the script
}
tasks.build.get().dependsOn("spotlessApply")
