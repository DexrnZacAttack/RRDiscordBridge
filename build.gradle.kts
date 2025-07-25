import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.java
import xyz.wagyourtail.unimined.api.minecraft.task.RemapJarTask
import java.time.Instant
import java.util.Locale

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

/* Source Sets */
val common: SourceSet by sourceSets.creating {
    blossom.javaSources {
        property("mod_id", modId)
        property("mod_name", modName)
        property("version", version.toString())
        property("license", license)
        property("author", author)
        property("description", description)
        property("homepage_url", homepageUrl)
        property("source_url", sourceUrl)
        property("modrinth_url", modrinthUrl)
        property("discord_url", discordUrl)
        property("logo", logo)
    }
}

/* Fabric */
val fabricCommon: SourceSet by sourceSets.creating
val fabricTrails: SourceSet by sourceSets.creating // Fabric 1.20.2+
val fabricNether: SourceSet by sourceSets.creating // Fabric 1.16-1.18.2
val fabricWild: SourceSet by sourceSets.creating // Fabric 1.19
val fabricAllay: SourceSet by sourceSets.creating // Fabric 1.19.1-1.19.2
val fabricVex: SourceSet by sourceSets.creating // Fabric 1.19.3-1.20.1
/* Bukkit */
val bukkitEmerald: SourceSet by sourceSets.creating {
    extra.set("deps", listOf<SourceSet>())
    extra.set("name", "Bukkit 1.3.1-1.7.8")
}
val bukkitFlat: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkitEmerald.output))
    extra.set("name", "Bukkit 1.1-1.2.5")
}
val poseidon: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkitFlat.output, bukkitEmerald.output))
    extra.set("name", "Project Poseidon")
}
val bukkitCookie: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkitFlat.output, bukkitEmerald.output))
    extra.set("name", "Bukkit b1.4-r1.0.1")
}
val bukkitRealms: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkitEmerald.output))
    extra.set("name", "Bukkit 1.7.9-1.11.2")
}

val bukkitColor: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkitRealms.output, bukkitEmerald.output))
    extra.set("name", "Bukkit 1.12-1.19.1")
}

val bukkitVex: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkitRealms.output, bukkitEmerald.output))
    extra.set("name", "Bukkit 1.19.2+")
}
/* Other */
val neoforge: SourceSet by sourceSets.creating
val paper: SourceSet by sourceSets.creating

listOf(bukkitEmerald).forEach {
    listOf(common).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkitCookie).forEach {
    listOf(common, bukkitFlat, bukkitEmerald).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(poseidon).forEach {
    listOf(common, bukkitFlat, bukkitEmerald).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkitFlat).forEach {
    listOf(common, bukkitEmerald).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkitRealms).forEach {
    listOf(common, bukkitEmerald).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkitColor).forEach {
    listOf(common, bukkitRealms, bukkitEmerald).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

listOf(bukkitVex).forEach {
    listOf(common, bukkitRealms, bukkitEmerald).forEach { sourceSet ->
        it.compileClasspath += sourceSet.output
        it.runtimeClasspath += sourceSet.output
    }
}

/* CompileOnly */
val mainCompileOnly: Configuration by configurations.creating
configurations.compileOnly.get().extendsFrom(mainCompileOnly)
/* Fabric */
val fabricCommonCompileOnly: Configuration by configurations.getting
val fabricTrailsCompileOnly: Configuration by configurations.getting
val fabricNetherCompileOnly: Configuration by configurations.getting
val fabricWildCompileOnly: Configuration by configurations.getting
val fabricAllayCompileOnly: Configuration by configurations.getting
val fabricVexCompileOnly: Configuration by configurations.getting
/* Bukkit */
val poseidonCompileOnly: Configuration by configurations.getting
val bukkitCookieCompileOnly: Configuration by configurations.getting
val bukkitFlatCompileOnly: Configuration by configurations.getting
val bukkitEmeraldCompileOnly: Configuration by configurations.getting
val bukkitRealmsCompileOnly: Configuration by configurations.getting
val bukkitColorCompileOnly: Configuration by configurations.getting
val bukkitVexCompileOnly: Configuration by configurations.getting
/* Other */
val neoforgeCompileOnly: Configuration by configurations.getting
val commonCompileOnly: Configuration by configurations.getting
val paperCompileOnly: Configuration by configurations.getting {
    extendsFrom(commonCompileOnly)
}

listOf(fabricTrailsCompileOnly, fabricCommonCompileOnly, fabricVexCompileOnly, fabricAllayCompileOnly, fabricWildCompileOnly, fabricNetherCompileOnly, neoforgeCompileOnly, paperCompileOnly, bukkitCookieCompileOnly, bukkitFlatCompileOnly, bukkitEmeraldCompileOnly, bukkitRealmsCompileOnly, bukkitColorCompileOnly, poseidonCompileOnly, bukkitVexCompileOnly).forEach {
    it.extendsFrom(commonCompileOnly)
}
/* Implementation */
val commonImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val modImplementation: Configuration by configurations.creating
/* Fabric */
val fabricTrailsModImplementation: Configuration by configurations.creating {
    extendsFrom(modImplementation)
}
val fabricNetherModImplementation: Configuration by configurations.creating {
    extendsFrom(modImplementation)
}
val fabricWildModImplementation: Configuration by configurations.creating {
    extendsFrom(modImplementation)
}
val fabricAllayModImplementation: Configuration by configurations.creating {
    extendsFrom(modImplementation)
}
val fabricVexModImplementation: Configuration by configurations.creating {
    extendsFrom(modImplementation)
}
val fabricCommonModImplementation: Configuration by configurations.creating {
    extendsFrom(fabricTrailsModImplementation)
}

/* Deps */
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
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // "Bukkit" 1.12.2+
}

dependencies {
    commonCompileOnly(libs.annotations)
    commonCompileOnly(libs.mixin)
    listOf("api-base", "lifecycle-events-v1", "message-api-v1", "networking-api-v1", "entity-events-v1", "command-api-v2").forEach {
        fabricWildModImplementation(fabricApi.fabricModule("fabric-$it", fabricWildVersion))
        fabricAllayModImplementation(fabricApi.fabricModule("fabric-$it", fabricAllayVersion))
        fabricVexModImplementation(fabricApi.fabricModule("fabric-$it", fabricVexVersion))
        mainCompileOnly(fabricApi.fabricModule("fabric-$it", fabricWildVersion))
        fabricTrailsModImplementation(fabricApi.fabricModule("fabric-$it", fabricVersion))
    }
    listOf("api-base", "lifecycle-events-v1", "networking-api-v1", "entity-events-v1", "command-api-v1").forEach {
        fabricNetherModImplementation(fabricApi.fabricModule("fabric-$it", fabricNetherVersion))
    }
//    paperCompileOnly("io.papermc.paper:paper-api:$minecraftVersion-$paperVersion")
    paperCompileOnly(libs.ignite.api)
    bukkitCookieCompileOnly("org.bukkit:bukkit:$bukkitCookieMcVersion-$bukkitCookieVersion")
    bukkitFlatCompileOnly("org.bukkit:bukkit:$bukkitFlatMcVersion-$bukkitFlatVersion")
    bukkitEmeraldCompileOnly("org.bukkit:bukkit:$bukkitEmeraldMcVersion-$bukkitEmeraldVersion")
    bukkitRealmsCompileOnly("org.bukkit:bukkit:$bukkitRealmsMcVersion-$bukkitRealmsVersion")
    bukkitColorCompileOnly("org.spigotmc:spigot-api:$bukkitColorMcVersion-$bukkitColorVersion")
    bukkitVexCompileOnly("org.spigotmc:spigot-api:$bukkitVexMcVersion-$bukkitVexVersion")
    poseidonCompileOnly("com.legacyminecraft.poseidon:poseidon-craftbukkit:${poseidonVersion}")
    mainCompileOnly("org.spigotmc:spigot-api:$bukkitVexMcVersion-$bukkitVexVersion")

    // so common can create the class instances of each
    fabricCommonCompileOnly(fabricTrails.output)
    fabricCommonCompileOnly(fabricNether.output)
    fabricCommonCompileOnly(fabricWild.output)
    fabricCommonCompileOnly(fabricAllay.output)
    fabricCommonCompileOnly(fabricVex.output)

    implementation("com.google.code.gson:gson:2.13.0")
    implementation("club.minnced:discord-webhooks:0.8.4")
    implementation("net.dv8tion:JDA:5.5.1")
    implementation("me.scarsz.jdaappender:jda5:1.2.3")
    implementation("org.slf4j:slf4j-jdk14:2.0.17")
    implementation("org.apache.logging.log4j:log4j-api:2.25.1")
    implementation("com.vdurmont:semver4j:3.1.0")
    fabricCommonCompileOnly("com.vdurmont:semver4j:3.1.0")
    fabricTrailsCompileOnly("com.vdurmont:semver4j:3.1.0")
    fabricNetherCompileOnly("com.vdurmont:semver4j:3.1.0")
    fabricWildCompileOnly("com.vdurmont:semver4j:3.1.0")
    fabricAllayCompileOnly("com.vdurmont:semver4j:3.1.0")
    fabricVexCompileOnly("com.vdurmont:semver4j:3.1.0")
}

/* Fabric setup */
data class FabricSourceSet(
    val minecraftVersion: String,
    val parchmentMinecraftVersion: String,
    val parchmentVersion: String
)

val fabricVersions: Map<SourceSet, FabricSourceSet> = mapOf(
    fabricTrails to FabricSourceSet(minecraftVersion, parchmentMinecraft, parchmentVersion),
    fabricCommon to FabricSourceSet(minecraftVersion, parchmentMinecraft, parchmentVersion),
    fabricNether to FabricSourceSet(fabricNetherMinecraftVersion, fabricNetherParchmentMinecraft, fabricNetherParchmentVersion),
    fabricWild to FabricSourceSet(fabricWildMinecraftVersion, fabricWildParchmentMinecraft, fabricWildParchmentVersion),
    fabricAllay to FabricSourceSet(fabricAllayMinecraftVersion, fabricAllayParchmentMinecraft, fabricAllayParchmentVersion),
    fabricVex to FabricSourceSet(fabricVexMinecraftVersion, fabricVexParchmentMinecraft, fabricVexParchmentVersion),
)

val bukkit = listOf(
    bukkitCookie,
    bukkitFlat,
    bukkitEmerald,
    bukkitRealms,
    bukkitColor,
    bukkitVex,
    poseidon
)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<RemapJarTask> {
    mixinRemap {
        enableBaseMixin()
        disableRefmap()
    }
}

unimined.minecraft {
    combineWith(common);
    version(minecraftVersion)
    mappings {
        parchment(parchmentMinecraft, parchmentVersion)
        mojmap()
    }

    defaultRemapJar = false
}

unimined.footgunChecks = false

fabricVersions.forEach { it ->
    unimined.minecraft(it.key) {
        combineWith(sourceSets.main.get())
        version(it.value.minecraftVersion)
        mappings {
            parchment(it.value.parchmentMinecraftVersion, it.value.parchmentVersion)
            mojmap()
        }

        fabric {
            loader(fabricLoaderVersion)
            if (it.key.name == "fabricCommon")
                accessWidener(project.projectDir.resolve("src/fabricCommon/resources/rrdiscordbridge.accesswidener"))
        }

        defaultRemapJar = true
    }

    tasks.register<ShadowJar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar") {
        dependsOn("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
        from(zipTree(tasks.getByName<Jar>("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile))
        archiveClassifier.set("${it.key.name}-relocated")
        relocate(
            "io.github.dexrnzacattack.rrdiscordbridge.mixins.vanilla",
            "io.github.dexrnzacattack.rrdiscordbridge.mixins.vanilla_intmdry"
        )
        relocate(
            "io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla",
            "io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla_intmdry"
        )
    }
}

/* Other */
unimined.minecraft(neoforge) {
    combineWith(sourceSets.main.get())
    neoForge {
        loader(neoForgeVersion)
    }
    defaultRemapJar = true
}

//unimined.minecraft(paper) {
//    combineWith(sourceSets.main.get())
//    combineWith(bukkitRealms)
//    accessTransformer {
//        // https://github.com/PaperMC/Paper/blob/main/build-data/paper.at
////        accessTransformer("${rootProject.projectDir}/src/paper/paper.at")
//    }
//    defaultRemapJar = true
//}

val commonShadowJar = tasks.register<ShadowJar>("commonShadowJar") {
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

tasks.named("build").configure {
    dependsOn("shadowJar")
}

tasks.named<JavaCompile>("compileCommonJava").configure {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

bukkit.forEach { set ->
    unimined.minecraft(set) {
        combineWith(sourceSets.main.get())
        defaultRemapJar = true
    }

    tasks.named<Jar>("${set.name}Jar").configure {
        dependsOn("${set.name}ShadowJar")
    }

    tasks.named<JavaCompile>("compile${set.name.replaceFirstChar { c -> c.uppercase() }}Java").configure {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
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

val ex = listOf(
    "natives/**",
    "com/sun/jna/**",
    "com/google/crypto/tink/**",
    "com/google/protobuf/**",
    "google/protobuf/**",
    "club/minnced/opus/util/*",
    "tomp2p/opuswrapper/*",
    "org/apache/logging/**",
    "/META-INF/services/org.apache.logging.log4j.util.PropertySource",
    "com/google/errorprone/**",
//    "com/fasterxml/jackson/**",
//    "META-INF/services/com.fasterxml.jackson.core.JsonFactory/**",
//    "META-INF/services/com.fasterxml.jackson.core.ObjectCodec/**",
//    "org/slf4j/**",
    "org/intellij/**",
    "org/jetbrains/**",
    "META-INF/versions/**"
)

/* Shadow Jar */
bukkit.forEach { set ->
    tasks.register<ShadowJar>("${set.name}ShadowJar") {
        archiveClassifier = set.extra["name"] as? String ?: set.name
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.getByName("commonRuntimeClasspath"))
        dependsOn(commonShadowJar)
        from(
            sourceSets.main.get().output,
            common.output,
            set.output,
            set.extra["deps"] as? List<SourceSetOutput>
        )

        if (set.name == "bukkitCookie")
            exclude("org/bukkit/craftbukkit/**") // stub for old craftbukkit

        exclude(ex)
    }
}

tasks.shadowJar {
        archiveClassifier = "Fabric-NeoForge"

        dependsOn("commonShadowJar")
    dependsOn("relocateFabricCommonJar")
    dependsOn("relocateFabricNetherJar")
    dependsOn("relocateFabricWildJar")
    dependsOn("relocateFabricAllayJar")
    dependsOn("relocateFabricVexJar")

    dependsOn("relocateFabricTrailsJar")
    from(
        zipTree(tasks.getByName<Jar>("relocateFabricCommonJar").archiveFile.get().asFile),
        zipTree(tasks.getByName<Jar>("relocateFabricTrailsJar").archiveFile.get().asFile),
        zipTree(tasks.getByName<Jar>("relocateFabricWildJar").archiveFile.get().asFile),
        zipTree(tasks.getByName<Jar>("relocateFabricAllayJar").archiveFile.get().asFile),
        zipTree(tasks.getByName<Jar>("relocateFabricVexJar").archiveFile.get().asFile),

        zipTree(tasks.getByName<Jar>("relocateFabricNetherJar").archiveFile.get().asFile),
            neoforge.output,
            )
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    relocate(
        "org.slf4j",
        "relocated.org.slf4j"
    )

    exclude(ex)
        exclude(listOf(
            "com/google/errorprone/**",
//            "com/fasterxml/jackson/**",
            "META-INF/services/com.fasterxml.jackson.core.JsonFactory/**",
            "META-INF/services/com.fasterxml.jackson.core.ObjectCodec/**",
            "META-INF/services/org.slf4j.spi.SLF4JServiceProvider/**",
            "javax/annotation/**",
            "com/google/gson/**",
//            "org/slf4j/**"
        ))
}


tasks.build.get().dependsOn("spotlessApply")
