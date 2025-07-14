import bukkit17McVersion
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import xyz.wagyourtail.unimined.api.minecraft.task.RemapJarTask
import java.time.Instant

plugins {
//    id("com.gradleup.shadow") version "9.0.0-beta15" apply false
    id("java")
    id("maven-publish")
    id("idea")
    id("eclipse")
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
    alias(libs.plugins.unimined)
}

subprojects {
    plugins.withId("com.gradleup.shadow") {
        tasks.withType<ShadowJar>().configureEach {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            archiveClassifier = ""
            exclude("META-INF")

            exclude("natives/**")
            exclude("com/sun/jna/**")
            exclude("com/google/crypto/tink/**")
            exclude("com/google/protobuf/**")
            exclude("google/protobuf/**")
            exclude("club/minnced/opus/util/*")
            exclude("tomp2p/opuswrapper/*")
        }


    tasks.named("build").configure {
        dependsOn("shadowJar")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.named<ProcessResources>("processResources").configure {
        filesMatching("plugin.yml") {
            expand(
                mapOf(
                "version" to version,
                "name" to modName,
                "author" to author,
                "description" to description,
                "homepage_url" to homepageUrl
                )
            )
        }
    }
}

    tasks.withType<Jar>().configureEach {
        archiveBaseName.set("${modName}-${project.name.replace(" ", "_")}")
    }
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
        licenseHeader("""/**
 * Copyright (c) 2025 $author
 * This project is Licensed under <a href="$sourceUrl/blob/main/LICENSE">$license</a>
 */""")
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
val bukkit11: SourceSet by sourceSets.creating
val bukkit13: SourceSet by sourceSets.creating
val bukkit17: SourceSet by sourceSets.creating

listOf(bukkit13).forEach {
    listOf(common).forEach { sourceSet ->
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
    extendsFrom(mainCompileOnly)
}
val poseidonCompileOnly: Configuration by configurations.getting
val bukkit11CompileOnly: Configuration by configurations.getting
val bukkit13CompileOnly: Configuration by configurations.getting
val bukkit17CompileOnly: Configuration by configurations.getting

listOf(fabricCompileOnly, neoforgeCompileOnly,
    paperCompileOnly, bukkit11CompileOnly, bukkit13CompileOnly, bukkit17CompileOnly, poseidonCompileOnly).forEach {
    it.extendsFrom(commonCompileOnly)
}
val modImplementation: Configuration by configurations.creating
val fabricModImplementation: Configuration by configurations.creating {
    extendsFrom(modImplementation)
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
}

unimined.minecraft {
    combineWith(common)
    version(minecraftVersion)
    mappings {
        parchment(parchmentMinecraft, parchmentVersion)
        mojmap()
        devFallbackNamespace("official")
    }
    defaultRemapJar = false
}

tasks.register<Jar>("commonJar") {
    archiveClassifier.set("common")
    from(common.output)
}

unimined.minecraft(fabric) {
    combineWith(sourceSets.main.get())
    fabric {
        loader(fabricLoaderVersion)
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
    combineWith(bukkit13)
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
    mainCompileOnly(libs.annotations)
    mainCompileOnly(libs.mixin)
    commonCompileOnly(libs.slf4j)
    listOf("api-base", "command-api-v2", "lifecycle-events-v1", "networking-api-v1").forEach {
        fabricModImplementation(fabricApi.fabricModule("fabric-$it", fabricVersion))
    }
    paperCompileOnly("io.papermc.paper:paper-api:$minecraftVersion-$paperVersion")
    paperCompileOnly(libs.ignite.api)
    bukkit13CompileOnly("org.bukkit:bukkit:$bukkit13McVersion-$bukkit13Version")
    bukkit13CompileOnly("org.bukkit:bukkit:$bukkit17McVersion-$bukkit17Version")
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

tasks.jar {
    dependsOn("relocateFabricJar")
    from(
        zipTree(tasks.getByName<Jar>("relocateFabricJar").archiveFile.get().asFile),
        neoforge.output,
        paper.output,
        bukkit13.output,
        bukkit11.output,
        bukkit17.output
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
}
tasks.build.get().dependsOn("spotlessApply")
