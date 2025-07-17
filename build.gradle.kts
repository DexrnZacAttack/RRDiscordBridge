import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.java
import xyz.wagyourtail.unimined.api.minecraft.task.RemapJarTask
import java.time.Instant

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
    }
}

val fabric: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(sourceSets.main.get().output))
    extra.set("name", "F")
}
val neoforge: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(sourceSets.main.get().output))
    extra.set("name", "NF")
}
val paper: SourceSet by sourceSets.creating
val bukkit13: SourceSet by sourceSets.creating {
    extra.set("deps", listOf<SourceSet>())
    extra.set("name", "Bukkit 1.3.1-1.7.8")
}
val bukkit11: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkit13.output))
    extra.set("name", "Bukkit 1.1-1.2.5")
}
val poseidon: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkit11.output, bukkit13.output))
    extra.set("name", "Project Poseidon")
}
val bukkit10: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkit11.output, bukkit13.output))
    extra.set("name", "Bukkit b1.4-r1.0.1")
}
val bukkit17: SourceSet by sourceSets.creating {
    extra.set("deps", listOf(bukkit13.output))
    extra.set("name", "Bukkit 1.7.9+")
}

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

/* CompileOnly */
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

listOf(fabricCompileOnly, neoforgeCompileOnly, paperCompileOnly, bukkit10CompileOnly, bukkit11CompileOnly, bukkit13CompileOnly, bukkit17CompileOnly, poseidonCompileOnly).forEach {
    it.extendsFrom(commonCompileOnly)
}
/* Implementation */
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

unimined.minecraft(bukkit10) {

    combineWith(sourceSets.main.get())
    defaultRemapJar = true
}

unimined.minecraft(bukkit11) {
    combineWith(sourceSets.main.get())
    defaultRemapJar = true
}

unimined.minecraft(bukkit13) {
    combineWith(sourceSets.main.get())
    defaultRemapJar = true
}

unimined.minecraft(bukkit17) {
    combineWith(sourceSets.main.get())
    defaultRemapJar = true
}

unimined.minecraft(poseidon) {
    combineWith(sourceSets.main.get())
    defaultRemapJar = true
}

val bukkit = listOf(
    bukkit10,
    bukkit11,
    bukkit13,
    bukkit17,
    poseidon
)

val loaders = listOf(
//    fabric,
    neoforge
)

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

    archiveClassifier = ""
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

tasks.named("build").configure {
    dependsOn("shadowJar")
}

listOf("compileBukkit10Java", "compileBukkit11Java", "compileBukkit13Java", "compileBukkit17Java", "compilePoseidonJava").forEach { name ->
    tasks.named<JavaCompile>(name).configure {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
}

tasks.named<JavaCompile>("compileCommonJava").configure {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

bukkit.forEach { set ->
    tasks.named("${set.name}Jar").configure {
        dependsOn("${set.name}ShadowJar")
    }
}

loaders.forEach { set ->
    tasks.named("${set.name}Jar").configure {
        dependsOn("${set.name}ShadowJar")
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

        if (set.name == "bukkit10")
            exclude("org/bukkit/craftbukkit/**") // stub for old craftbukkit

        exclude(ex)
    }
}

loaders.forEach { set ->
    tasks.register<ShadowJar>("${set.name}ShadowJar") {
        archiveClassifier = set.extra["name"] as? String ?: set.name
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.getByName("commonRuntimeClasspath"))

        dependsOn("relocateFabricJar")
        dependsOn("commonShadowJar")
        from(
            sourceSets.main.get().output,
            common.output,
            set.output,
            set.extra["deps"] as? List<SourceSetOutput>,
            zipTree(tasks.getByName<Jar>("relocateFabricJar").archiveFile.get().asFile),
        )

        exclude(ex)
        exclude(listOf(
            "com/google/errorprone/**",
            "com/fasterxml/jackson/**",
            "META-INF/services/com.fasterxml.jackson.core.JsonFactory/**",
            "META-INF/services/com.fasterxml.jackson.core.ObjectCodec/**",
            "META-INF/services/org.slf4j.spi.SLF4JServiceProvider/**",
            "javax/annotation/**",
            "com/google/gson/**",
            "org/slf4j/**"
        ))
    }
}

tasks.build.get().dependsOn("spotlessApply")
