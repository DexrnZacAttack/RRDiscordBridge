import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
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
        property("source_url", sourceUrl)
        property("modrinth_url", modrinthUrl)
        property("discord_url", discordUrl)
        property("logo", logo)
    }
}

// it's FUN!!!!
fun getCompileOnly(n: String) = configurations.findByName(n) ?: configurations.create(n)

/** Creates a SourceSet
 *
 * @param n SourceSet name
 * @param f Subfolder (under src) where the SourceSet code is stored
 *
 * @return The source set
 */
fun createSourceSet(n: String, f: String): SourceSet {
    val s = sourceSets.create(n)
    s.java.srcDir("src/${f}/${n}/java")
    s.resources.srcDir("src/${f}/${n}/resources")
    return s
}

/* Fabric */
data class FabricProj(
    val name: String,
    val deps: List<String>,
    val apiVersion: String,
    val minecraftVersion: String,
    val parchmentMinecraft: String,
    val parchmentVersion: String,
    val fabricApiDeps: List<String>
)

val fabricProjects = listOf(
    FabricProj("fabricCommon", listOf(), fabricVersion, minecraftVersion, parchmentMinecraft, parchmentVersion, listOf("api-base", "lifecycle-events-v1", "message-api-v1", "networking-api-v1", "entity-events-v1", "command-api-v2")),
    FabricProj("fabricEntrypoint", listOf(), fabricVersion, minecraftVersion, parchmentMinecraft, parchmentVersion, listOf("api-base", "lifecycle-events-v1", "message-api-v1", "networking-api-v1", "entity-events-v1", "command-api-v2")),

    FabricProj("fabricNether", listOf(), fabricNetherVersion, fabricNetherMinecraftVersion, fabricNetherParchmentMinecraft, fabricNetherParchmentVersion, listOf("api-base", "lifecycle-events-v1", "networking-api-v1", "entity-events-v1", "command-api-v1")), // Fabric 1.16-1.18.2
    FabricProj("fabricWild", listOf("fabricVex"), fabricWildVersion, fabricWildMinecraftVersion, fabricWildParchmentMinecraft, fabricWildParchmentVersion, listOf("api-base", "lifecycle-events-v1", "message-api-v1", "networking-api-v1", "entity-events-v1", "command-api-v2")), // Fabric 1.19
    FabricProj("fabricAllay", listOf("fabricVex"), fabricAllayVersion, fabricAllayMinecraftVersion, fabricAllayParchmentMinecraft, fabricAllayParchmentVersion, listOf("api-base", "lifecycle-events-v1", "message-api-v1", "networking-api-v1", "entity-events-v1", "command-api-v2")), // Fabric 1.19.1-1.19.2
    FabricProj("fabricVex", listOf(), fabricVexVersion, fabricVexMinecraftVersion, fabricVexParchmentMinecraft, fabricVexParchmentVersion, listOf("api-base", "lifecycle-events-v1", "message-api-v1", "networking-api-v1", "entity-events-v1", "command-api-v2")), // Fabric 1.19.3-1.20.1
    FabricProj("fabricTrade", listOf(), fabricVersion, minecraftVersion, parchmentMinecraft, parchmentVersion, listOf("api-base", "lifecycle-events-v1", "message-api-v1", "networking-api-v1", "entity-events-v1", "command-api-v2")), // Fabric 1.20.2+
)

val fabricSourceSets: Map<String, SourceSet> = fabricProjects.associate { p ->
    p.name to createSourceSet(p.name, "fabric")
}

val fabricCompileOnly: Map<String, Configuration> = fabricProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val fabricVersions: Map<SourceSet, FabricProj> = fabricProjects.associate { p ->
    fabricSourceSets.getValue(p.name) to FabricProj(p.name, p.deps, p.apiVersion, p.minecraftVersion, p.parchmentMinecraft, p.parchmentVersion, p.fabricApiDeps)
}

/* Bukkit */
data class BukkitProj(
    val name: String,
    val deps: List<String>,
    val jarName: String,
    val mvn: String
)

// TODO: once we merge bukkit into one shadowjar we can make bukkitCommon not build it's own jar and deps will not be so messy
val bukkitProjects = listOf(
    BukkitProj("bukkitCommon", listOf(), "Bukkit Common", "org.bukkit:bukkit:$bukkitEmeraldMcVersion-$bukkitEmeraldVersion"),

    BukkitProj("poseidon", listOf("bukkitFlat"), "Project Poseidon", "com.legacyminecraft.poseidon:poseidon-craftbukkit:${poseidonVersion}"),
    BukkitProj("bukkitCake", listOf(), "Bukkit b1.2_01-b1.3_01", "org.bukkit:bukkit:$bukkitCookieMcVersion-$bukkitCookieVersion"),
    BukkitProj("bukkitCookie", listOf("bukkitFlat"), "Bukkit b1.4-r1.0.1", "org.bukkit:bukkit:$bukkitCookieMcVersion-$bukkitCookieVersion"),
    BukkitProj("bukkitFlat", listOf(), "Bukkit 1.1-1.2.5", "org.bukkit:bukkit:$bukkitFlatMcVersion-$bukkitFlatVersion"),
    BukkitProj("bukkitEmerald", listOf(), "Bukkit 1.3.1-1.7.8", "org.bukkit:bukkit:$bukkitEmeraldMcVersion-$bukkitEmeraldVersion"),
    BukkitProj("bukkitRealms", listOf("bukkitEmerald"), "Bukkit 1.7.9-1.11.2", "org.bukkit:bukkit:$bukkitRealmsMcVersion-$bukkitRealmsVersion"),
    BukkitProj("bukkitColor", listOf("bukkitEmerald", "bukkitRealms"), "Bukkit 1.12-1.19.1", "org.spigotmc:spigot-api:$bukkitColorMcVersion-$bukkitColorVersion"),
    BukkitProj("bukkitVex", listOf("bukkitEmerald", "bukkitRealms"), "Bukkit 1.19.2+", "org.spigotmc:spigot-api:$bukkitVexMcVersion-$bukkitVexVersion")
)

val bukkitSourceSets: Map<String, SourceSet> = bukkitProjects.associate { p ->
    p.name to createSourceSet(p.name, "bukkit")
}

val bukkitCompileOnly: Map<String, Configuration> = bukkitProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val bukkitVersions: Map<SourceSet, BukkitProj> = bukkitProjects.associate { p ->
    bukkitSourceSets.getValue(p.name) to BukkitProj(p.name, p.deps, p.jarName, p.mvn)
}

bukkitProjects.forEach { p ->
    val it = bukkitSourceSets.getValue(p.name)
    val d = listOf(common, bukkitSourceSets.getValue("bukkitCommon")) + p.deps.map { depName -> bukkitSourceSets.getValue(depName) }
    d.forEach { out ->
        if (out.name != p.name) {
            it.compileClasspath += out.output
            it.runtimeClasspath += out.output
        }
    }
}


/* NeoForge */
data class ForgeProj(
    val name: String,
    val minecraftVersion: String,
    val loaderVersion: String,
)

val neoforgeProjects = listOf(
    ForgeProj("neoforgeCommon", neoforgePotMinecraftVersion, neoforgePotVersion),
    ForgeProj("neoforgeEntrypoint", neoforgePotMinecraftVersion, neoforgePotVersion),

    ForgeProj("neoforgePot", neoforgePotMinecraftVersion, neoforgePotVersion),
    ForgeProj("neoforgeTrade", neoforgeTradeMinecraftVersion, neoforgeTradeVersion),
)

val neoforgeSourceSets: Map<String, SourceSet> = neoforgeProjects.associate { p ->
    p.name to createSourceSet(p.name, "neoforge")
}

val neoforgeCompileOnly: Map<String, Configuration> = neoforgeProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val neoforgeVersions: Map<SourceSet, ForgeProj> = neoforgeProjects.associate { p ->
    neoforgeSourceSets.getValue(p.name) to ForgeProj(p.name, p.minecraftVersion, p.loaderVersion)
}

/* Forge */
val forgeProjects = listOf(
    ForgeProj("forgeCommon", forgePotMinecraftVersion, forgePotVersion),
    ForgeProj("forgeEntrypoint", forgePotMinecraftVersion, forgePotVersion),

    ForgeProj("forgePot", forgePotMinecraftVersion, forgePotVersion),
    ForgeProj("forgeTrade", forgeTradeMinecraftVersion, forgeTradeVersion),
    ForgeProj("forgeTrails", forgeTrailsMinecraftVersion, forgeTrailsVersion),
)

val forgeSourceSets: Map<String, SourceSet> = forgeProjects.associate { p ->
    p.name to createSourceSet(p.name, "forge")
}

val forgeCompileOnly: Map<String, Configuration> = forgeProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val forgeVersions: Map<SourceSet, ForgeProj> = forgeProjects.associate { p ->
    forgeSourceSets.getValue(p.name) to ForgeProj(p.name, p.minecraftVersion, p.loaderVersion)
}

val extension: SourceSet by sourceSets.creating
val extensionCompileOnly: Configuration by configurations.getting

/* CompileOnly */
val mainCompileOnly: Configuration by configurations.creating
configurations.compileOnly.get().extendsFrom(mainCompileOnly)

val commonCompileOnly: Configuration by configurations.getting

(fabricCompileOnly + bukkitCompileOnly + neoforgeCompileOnly).forEach {
    it.value.extendsFrom(commonCompileOnly)
}

/* Implementation */
val commonImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val modImplementation: Configuration by configurations.creating

/* Fabric */
val fabricModImpls: Map<String, Configuration> = fabricProjects.associate { p ->
    p.name to configurations.create("${p.name}ModImplementation")
}

fabricModImpls.values.forEach { cfg ->
    cfg.extendsFrom(modImplementation)
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
    flatDir { // OLDER BUKKIT
        dirs("libs")
    }
}

dependencies {
    commonCompileOnly(libs.annotations)
    commonCompileOnly(libs.mixin)

    // bugkit
    bukkitProjects.forEach { p ->
        add(bukkitCompileOnly.getValue(p.name).name, p.mvn)
    }

    bukkitSourceSets.forEach { (n, _) ->
        if (n != "bukkitCommon") {
            // all can access common
            add(bukkitCompileOnly.getValue(n).name, bukkitSourceSets.getValue("bukkitCommon").output)
        }
        if (n == "bukkitCake" && useLocalBukkitCake) {
            println("Using local jar for bukkitCake")
            add(bukkitCompileOnly.getValue(n).name, files("libs/bukkitCake.jar"))
        }
    }

    // fabrick
    fabricSourceSets.forEach { (n, s) ->
        if (n != "fabricEntrypoint") {
            // entrypoint can access all
            add(fabricCompileOnly.getValue("fabricEntrypoint").name, s.output)
        }
        if (n != "fabricCommon") {
            // all can access common
            add(fabricCompileOnly.getValue(n).name, fabricSourceSets.getValue("fabricCommon").output)
        }
    }

    // deps
    fabricProjects.forEach { p ->
        p.deps.map { depName -> fabricSourceSets.getValue(depName) }.forEach { d ->
            if (d.name != p.name) {
                add(fabricCompileOnly.getValue(p.name).name, d.output)
            }
        }
    }

    // api
    fabricVersions.forEach { v ->
        v.value.fabricApiDeps.forEach { mod ->
            add(fabricModImpls.getValue(v.value.name).name, fabricApi.fabricModule("fabric-$mod", v.value.apiVersion))
        }
    }

    // neofroge
    neoforgeSourceSets.forEach { (n, s) ->
        if (n != "neoforgeEntrypoint") {
            // entrypoint can access all
            add(neoforgeCompileOnly.getValue("neoforgeEntrypoint").name, s.output)
        }
        if (n != "neoforgeCommon") {
            // all can access common
            add(neoforgeCompileOnly.getValue(n).name, neoforgeSourceSets.getValue("neoforgeCommon").output)
        }
    }

    // froge
    forgeSourceSets.forEach { (n, s) ->
        if (n != "forgeEntrypoint") {
            // entrypoint can access all
            add(forgeCompileOnly.getValue("forgeEntrypoint").name, s.output)
        }
        if (n != "forgeCommon") {
            // all can access common
            add(forgeCompileOnly.getValue(n).name, forgeSourceSets.getValue("forgeCommon").output)
        }
    }

    // universal deps
    (fabricCompileOnly + neoforgeCompileOnly + forgeCompileOnly).forEach { (_, c) ->
        add(c.name, "com.vdurmont:semver4j:3.1.0")
    }

    extensionCompileOnly(common.output)
    extensionCompileOnly("club.minnced:discord-webhooks:0.8.4")
    extensionCompileOnly("net.dv8tion:JDA:6.0.0")
    extensionCompileOnly("com.vdurmont:semver4j:3.1.0")
    extensionCompileOnly("com.google.code.gson:gson:2.13.0")

    // common deps
    implementation("com.google.code.gson:gson:2.13.0")
    implementation("org.danilopianini:gson-extras:3.3.0")
    implementation("club.minnced:discord-webhooks:0.8.4")
    implementation("net.dv8tion:JDA:6.0.0")
    implementation("me.scarsz.jdaappender:jda5:1.2.3")
    implementation("org.slf4j:slf4j-jdk14:2.0.17")
    implementation("org.apache.logging.log4j:log4j-api:2.25.1")
    implementation("com.vdurmont:semver4j:3.1.0")
    implementation("org.apache.commons:commons-lang3:3.19.0")
}

/** Unimined */
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
            parchment(it.value.parchmentMinecraft, it.value.parchmentVersion)
            mojmap()
        }

        fabric {
            loader(fabricLoaderVersion)
            if (it.key.name == "fabricEntrypoint")
                accessWidener(project.projectDir.resolve("src/fabric/fabricEntrypoint/resources/rrdiscordbridge.accesswidener"))
        }

        defaultRemapJar = true
    }

    tasks.named<Jar>("${it.key.name}Jar").configure {
        destinationDirectory.set(layout.buildDirectory.dir("tmp/fabric"))
    }

    tasks.named<RemapJarTask>("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar") {
        asJar.archiveClassifier.set("${it.key.name}-remapped")
        asJar.destinationDirectory.set(layout.buildDirectory.dir("tmp/fabric/remapped"))
    }

    tasks.register<ShadowJar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar") {
        dependsOn("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
        from(zipTree(tasks.getByName<Jar>("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile))
        archiveClassifier.set("${it.key.name}-relocated")
        destinationDirectory.set(layout.buildDirectory.dir("tmp/fabric/relocated"))
        relocate(
            "me.dexrn.rrdiscordbridge.mixins.vanilla",
            "me.dexrn.rrdiscordbridge.mixins.vanilla.intermediary"
        )
        relocate(
            "me.dexrn.rrdiscordbridge.impls.vanilla",
            "me.dexrn.rrdiscordbridge.impls.vanilla.intermediary"
        )
    }
}

forgeVersions.forEach { it ->
    unimined.minecraft(it.key) {
        combineWith(sourceSets.main.get())
        version(it.value.minecraftVersion)
        minecraftForge {
            loader(it.value.loaderVersion)
        }
        defaultRemapJar = true
    }

    tasks.named<Jar>("${it.key.name}Jar").configure {
        destinationDirectory.set(layout.buildDirectory.dir("tmp/forge"))
    }

    tasks.named<RemapJarTask>("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar") {
        asJar.archiveClassifier.set("${it.key.name}-remapped")
        asJar.destinationDirectory.set(layout.buildDirectory.dir("tmp/forge/remapped"))
        mixinRemap {
            disableRefmap()
        }
    }

    tasks.register<ShadowJar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar") {
        dependsOn("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
        from(zipTree(tasks.getByName<Jar>("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile))
        archiveClassifier.set("${it.key.name}-relocated")
        destinationDirectory.set(layout.buildDirectory.dir("tmp/forge/relocated"))
        relocate(
            "me.dexrn.rrdiscordbridge.mixins.vanilla",
            "me.dexrn.rrdiscordbridge.mixins.vanilla.srg"
        )
        relocate(
            "me.dexrn.rrdiscordbridge.impls.vanilla",
            "me.dexrn.rrdiscordbridge.impls.vanilla.srg"
        )
    }
}

neoforgeVersions.forEach { it ->
    unimined.minecraft(it.key) {
        combineWith(sourceSets.main.get())
        version(it.value.minecraftVersion)
        neoForge {
            loader(it.value.loaderVersion)
        }
        defaultRemapJar = true
    }

    tasks.named<RemapJarTask>("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar") {
        asJar.archiveClassifier.set("${it.key.name}-remapped")
        asJar.destinationDirectory.set(layout.buildDirectory.dir("tmp/neoforge/remapped"))
    }

    tasks.named<Jar>("${it.key.name}Jar").configure {
        destinationDirectory.set(layout.buildDirectory.dir("tmp/neoforge"))
    }
}

bukkitVersions.forEach { set ->
    // Dunno how flexible Unimined is with a custom bukkit source, so I decided just to set it up myself
//    unimined.minecraft(set.key) {
//        combineWith(sourceSets.main.get())
////        defaultRemapJar = true
//    }

    tasks.register<Jar>("${set.value.name}Jar") {
        println("Registering ${set.value.name}")

        from(sourceSets.main.get().output)

        group = JavaBasePlugin.BUILD_TASK_NAME
//        archiveBaseName.set(set.value.jarName)

//        destinationDirectory.set(layout.buildDirectory.dir("src/${set.value.name}"))

        dependsOn("${set.value.name}ShadowJar")
    }

    tasks.named<JavaCompile>("compile${set.value.name.replaceFirstChar { c -> c.uppercase() }}Java").configure {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
}

/** Main */
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"

//    if (JavaVersion.current().isJava9Compatible) {
//        options.release.set(8)
//    }
}

tasks.register("buildBukkit") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    bukkitVersions.forEach { set ->
        dependsOn("${set.value.name}Jar")
    }
}

tasks.register("buildMod") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    dependsOn("shadowJar")
}

tasks.named("build").configure {
    dependsOn("buildBukkit")
    dependsOn("buildMod")
}

tasks.register<Jar>("buildCommon") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    dependsOn("commonShadowJar")
}

tasks.named<Jar>("jar").configure {
    destinationDirectory.set(layout.buildDirectory.dir("tmp/rrdb"))
}

tasks.named<JavaCompile>("compileCommonJava").configure {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
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

/** ShadowJar */
val ex = listOf(
    "natives/**",
    "com/sun/jna/**",
    "com/google/crypto/tink/**",
    "com/google/protobuf/**",
    "google/protobuf/**",
    "club/minnced/opus/util/*",
    "tomp2p/opuswrapper/*",
//    "org/apache/logging/**",
    "/META-INF/services/org.apache.logging.log4j.util.PropertySource",
    "com/google/errorprone/**",
//    "com/fasterxml/jackson/**",
//    "META-INF/services/com.fasterxml.jackson.core.JsonFactory/**",
//    "META-INF/services/com.fasterxml.jackson.core.ObjectCodec/**",
//    "org/slf4j/**",
    "org/intellij/**",
    "org/jetbrains/**",
//    "META-INF/versions/**"
)

val commonShadowJar = tasks.register<ShadowJar>("commonShadowJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to modName,
                "Specification-Version" to version,
                "Specification-Vendor" to "DexrnZacAttack",
                "Implementation-Version" to version,
                "Implementation-Vendor" to "DexrnZacAttack",
                "Implementation-Timestamp" to Instant.now().toString(),
                "FMLCorePluginContainsFMLMod" to "true",
                "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                "MixinConfigs" to "$modId.mixins.vanilla.json,$modId.mixins.forge.json"
            )
        )
    }

    from(common.output, extension.output)

    from(listOf("README.md", "LICENSE")) {
        into("META-INF")
    }
}

bukkitVersions.forEach { set ->
    tasks.register<ShadowJar>("${set.value.name}ShadowJar") {
        archiveClassifier = set.value.jarName
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(project.configurations.getByName("commonRuntimeClasspath"))
        dependsOn(commonShadowJar)

        from(listOf(sourceSets.main.get().output, extension.output, common.output, bukkitSourceSets.getValue("bukkitCommon").output, set.key.output)
        + set.value.deps.map { d ->
            bukkitSourceSets.getValue(d).output
        })

        if (set.value.name == "bukkitCookie" || set.value.name == "bukkitCake")
            exclude("org/bukkit/**") // stub for old craftbukkit

        relocate(
            "com.google.gson",
            "relocated.com.google.gson"
        )
        exclude(ex)
    }
}

tasks.shadowJar {
    archiveClassifier = "Fabric-Forge-NeoForge"

    dependsOn("commonShadowJar")

    fabricVersions.forEach { it ->
        dependsOn("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
    }
    forgeVersions.forEach { it ->
        dependsOn("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
    }
    from(
        extension.output,
        fabricVersions.map { it ->
            println("Shadowing ${it.key.name}");
            zipTree(tasks.getByName<Jar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile)
        },
        forgeVersions.map { it ->
            println("Shadowing ${it.key.name}");
            zipTree(tasks.getByName<Jar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile)
        },
        neoforgeSourceSets.map { it ->
            println("Shadowing ${it.key}");
            it.value.output
        },
        )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    relocate(
        "org.slf4j",
        "relocated.org.slf4j"
    )

    relocate(
        "kotlin",
        "relocated.kotlin"
    )

    relocate(
        "org.apache.commons.lang3",
        "relocated.org.apache.commons.lang3"
    )

//    relocate(
//        "org.apache.logging",
//        "relocated.org.apache.logging"
//    )

    relocate(
        "com.fasterxml.jackson",
        "relocated.com.fasterxml.jackson"
    )

    relocate(
        "com.google.gson",
        "relocated.com.google.gson"
    )

    exclude(ex)
        exclude(listOf(
            "org/apache/logging/**",
            "com/google/errorprone/**",
//            "com/fasterxml/jackson/**",
            "META-INF/services/com.fasterxml.jackson.core.JsonFactory/**",
            "META-INF/services/com.fasterxml.jackson.core.ObjectCodec/**",
            "META-INF/services/org.slf4j.spi.SLF4JServiceProvider/**",
            "javax/annotation/**",
//            "org/slf4j/**"
        ))
}

/** Extra */
tasks.build.get().dependsOn("spotlessApply")

tasks.register<Javadoc>("genJavadoc") {
    group = JavaBasePlugin.DOCUMENTATION_GROUP

    source = common.allJava
    classpath = files(common.output, common.compileClasspath)

    setDestinationDir(file("javadoc/generated"))

    val css = file("javadoc/javadoc.css")

    if (css.exists())
        options {
            this as StandardJavadocDocletOptions
            stylesheetFile = css;
        }
}
