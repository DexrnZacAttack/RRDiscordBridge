import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.java
import xyz.wagyourtail.unimined.api.minecraft.task.RemapJarTask
import java.time.Instant
// this build script is a mess
plugins {
    id("java")
    id("maven-publish")
    id("idea")
    id("eclipse")
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
    alias(libs.plugins.unimined)
    alias(libs.plugins.jvmdowngrader)
}

unimined.useGlobalCache = false
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
sourceSets.main.configure {
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
    if (!n.endsWith("Entrypoint")) {
        s.java.srcDir("src/${f}/${n}/java")
        s.resources.srcDir("src/${f}/${n}/resources")
    } else {
        s.java.srcDir("src/entrypoint/${n}/java")
        s.resources.srcDir("src/entrypoint/${n}/resources")
    }
    return s
}

/* Fabric */
data class FabricProj(
    val name: String,
    val deps: List<String>,
    val apiVersion: String,
    val minecraftVersion: String,
    val parchmentMinecraft: String?,
    val parchmentVersion: String?,
    val fabricApiDeps: List<String>
)

val fabricProjects = listOf(
    FabricProj(
        "fabricCommon",
        listOf(),
        fabricTradeVersion,
        fabricTradeMinecraftVersion,
        fabricTradeParchmentMinecraft,
        fabricTradeParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ),
    FabricProj(
        "fabricEntrypoint",
        listOf(),
        fabricCopperVersion,
        fabricCopperMinecraftVersion,
        fabricCopperParchmentMinecraft,
        fabricCopperParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ),

    FabricProj(
        "fabricNether",
        listOf(),
        fabricNetherVersion,
        fabricNetherMinecraftVersion,
        fabricNetherParchmentMinecraft,
        fabricNetherParchmentVersion,
        listOf("api-base", "lifecycle-events-v1", "networking-api-v1", "entity-events-v1", "command-api-v1")
    ), // Fabric 1.16-1.18.2
    FabricProj(
        "fabricWild",
        listOf("fabricVex"),
        fabricWildVersion,
        fabricWildMinecraftVersion,
        fabricWildParchmentMinecraft,
        fabricWildParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ), // Fabric 1.19
    FabricProj(
        "fabricAllay",
        listOf("fabricVex"),
        fabricAllayVersion,
        fabricAllayMinecraftVersion,
        fabricAllayParchmentMinecraft,
        fabricAllayParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ), // Fabric 1.19.1-1.19.2
    FabricProj(
        "fabricVex",
        listOf(),
        fabricVexVersion,
        fabricVexMinecraftVersion,
        fabricVexParchmentMinecraft,
        fabricVexParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ), // Fabric 1.19.3-1.20.1
    FabricProj(
        "fabricTrade",
        listOf(),
        fabricTradeVersion,
        fabricTradeMinecraftVersion,
        fabricTradeParchmentMinecraft,
        fabricTradeParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ), // Fabric 1.20.2
    FabricProj(
        "fabricPot",
        listOf(),
        fabricPotVersion,
        fabricPotMinecraftVersion,
        fabricPotParchmentMinecraft,
        fabricPotParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ), // Fabric 1.20.3-1.20.4
    FabricProj(
        "fabricPaws",
        listOf(),
        fabricPawsVersion,
        fabricPawsMinecraftVersion,
        null,
        null,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ), // Fabric 1.20.5-1.21.8
    FabricProj(
        "fabricCopper",
        listOf(),
        fabricCopperVersion,
        fabricCopperMinecraftVersion,
        fabricCopperParchmentMinecraft,
        fabricCopperParchmentVersion,
        listOf(
            "api-base",
            "lifecycle-events-v1",
            "message-api-v1",
            "networking-api-v1",
            "entity-events-v1",
            "command-api-v2"
        )
    ), // Fabric 1.21.9-Latest
)

val fabricSourceSets: Map<String, SourceSet> = fabricProjects.associate { p ->
    p.name to createSourceSet(p.name, "fabric")
}

val fabricCompileOnly: Map<String, Configuration> = fabricProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val fabricVersions: Map<SourceSet, FabricProj> = fabricProjects.associate { p ->
    fabricSourceSets.getValue(p.name) to FabricProj(
        p.name,
        p.deps,
        p.apiVersion,
        p.minecraftVersion,
        p.parchmentMinecraft,
        p.parchmentVersion,
        p.fabricApiDeps
    )
}

/* Bukkit */
data class BukkitProj(
    val name: String,
    val deps: List<String>,
    val mvn: String
)

val bukkitProjects = listOf(
    BukkitProj(
        "bukkitCommon",
        listOf(),
        "org.bukkit:bukkit:$bukkitEmeraldMcVersion-$bukkitEmeraldVersion"
    ),
    BukkitProj(
        "bukkitEntrypoint",
        listOf("poseidon", "bukkitCake", "bukkitCookie", "bukkitFlat", "bukkitEmerald", "bukkitRealms", "bukkitColor", "bukkitVex"),
        "org.bukkit:bukkit:$bukkitEmeraldMcVersion-$bukkitEmeraldVersion"
    ),

    BukkitProj(
        "poseidon",
        listOf("bukkitFlat"),
        "com.legacyminecraft.poseidon:poseidon-craftbukkit:${poseidonVersion}"
    ),
    BukkitProj(
        "bukkitCake",
        listOf(),
        "org.bukkit:bukkit:$bukkitCookieMcVersion-$bukkitCookieVersion"
    ),
    BukkitProj(
        "bukkitCookie",
        listOf("bukkitFlat"),
        "org.bukkit:bukkit:$bukkitCookieMcVersion-$bukkitCookieVersion"
    ),
    BukkitProj("bukkitFlat", listOf(),  "org.bukkit:bukkit:$bukkitFlatMcVersion-$bukkitFlatVersion"),
    BukkitProj(
        "bukkitEmerald",
        listOf(),
        "org.bukkit:bukkit:$bukkitEmeraldMcVersion-$bukkitEmeraldVersion"
    ),
    BukkitProj(
        "bukkitRealms",
        listOf("bukkitEmerald"),
        "org.bukkit:bukkit:$bukkitRealmsMcVersion-$bukkitRealmsVersion"
    ),
    BukkitProj(
        "bukkitColor",
        listOf("bukkitEmerald", "bukkitRealms"),
        "org.spigotmc:spigot-api:$bukkitColorMcVersion-$bukkitColorVersion"
    ),
    BukkitProj(
        "bukkitVex",
        listOf("bukkitEmerald", "bukkitRealms"),
        "org.spigotmc:spigot-api:$bukkitVexMcVersion-$bukkitVexVersion"
    )
)

val bukkitSourceSets: Map<String, SourceSet> = bukkitProjects.associate { p ->
    p.name to createSourceSet(p.name, "bukkit")
}

val bukkitCompileOnly: Map<String, Configuration> = bukkitProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val bukkitVersions: Map<SourceSet, BukkitProj> = bukkitProjects.associate { p ->
    bukkitSourceSets.getValue(p.name) to BukkitProj(p.name, p.deps, p.mvn)
}

bukkitProjects.forEach { p ->
    val it = bukkitSourceSets.getValue(p.name)
    val d = listOf(
        sourceSets.main.get(),
        bukkitSourceSets.getValue("bukkitCommon")
    ) + p.deps.map { depName -> bukkitSourceSets.getValue(depName) }
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
    val remap: Boolean,
    val deps: List<String> = listOf(),
    val mcpVersion: String = "",
    val mcpBuild: String = ""
)

val neoforgeProjects = listOf(
    ForgeProj("neoforgeCommon", neoforgePotMinecraftVersion, neoforgePotVersion, false),
    ForgeProj("neoforgeEntrypoint", neoforgePotMinecraftVersion, neoforgePotVersion, false),

    ForgeProj("neoforgeCopper", neoforgeCopperMinecraftVersion, neoforgeCopperVersion, false, listOf("neoforgePot")),
    ForgeProj("neoforgePot", neoforgePotMinecraftVersion, neoforgePotVersion, false),
    ForgeProj("neoforgeTrade", neoforgeTradeMinecraftVersion, neoforgeTradeVersion, false),
    )

val neoforgeSourceSets: Map<String, SourceSet> = neoforgeProjects.associate { p ->
    p.name to createSourceSet(p.name, "neoforge")
}

val neoforgeCompileOnly: Map<String, Configuration> = neoforgeProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val neoforgeVersions: Map<SourceSet, ForgeProj> = neoforgeProjects.associate { p ->
    neoforgeSourceSets.getValue(p.name) to ForgeProj(p.name, p.minecraftVersion, p.loaderVersion, p.remap, p.deps)
}

/* Forge */
val forgeProjects = listOf(
    ForgeProj("forgeCommon", forgePotMinecraftVersion, forgePotVersion, true),
    ForgeProj("forgeEntrypoint", forgeAllayHotfixMinecraftVersion, forgeAllayHotfixVersion, true, listOf("forgeCavesEntrypoint", "forgePillageEntrypoint", "forgeAquaticEntrypoint")),
    ForgeProj("forgeCavesEntrypoint", forgeAllayHotfixMinecraftVersion, forgeAllayHotfixVersion, true),
    ForgeProj("forgePillageEntrypoint", forgeNetherMinecraftVersion, forgeNetherVersion, true),
    ForgeProj("forgeAquaticEntrypoint", forgeAquaticMinecraftVersion, forgeAquaticVersion, true, listOf("mc"), "20180921-1.13", "snapshot"),

    ForgeProj("forgeCopper", forgeCopperMinecraftVersion, forgeCopperVersion, false, listOf("forgeSkies", "forgePaws")),
    ForgeProj("forgeSkies", forgeSkiesMinecraftVersion, forgeSkiesVersion, false, listOf("forgePaws")),
    ForgeProj("forgePaws", forgePawsMinecraftVersion, forgePawsVersion, false),
    ForgeProj("forgePot", forgePotMinecraftVersion, forgePotVersion, true),
    ForgeProj("forgeTrade", forgeTradeMinecraftVersion, forgeTradeVersion, true),
    ForgeProj("forgeAllayHotfix", forgeAllayHotfixMinecraftVersion, forgeAllayHotfixVersion, true),
    ForgeProj("forgeAllay", forgeAllayMinecraftVersion, forgeAllayVersion, true, listOf("forgeCliffs")),
    ForgeProj("forgeWild", forgeWildMinecraftVersion, forgeWildVersion, true, listOf("forgeCliffs")),
    ForgeProj("forgeCliffs", forgeCliffsMinecraftVersion, forgeCliffsVersion, true),
    ForgeProj("forgeCaves", forgeCavesMinecraftVersion, forgeCavesVersion, true, listOf("forgeCliffs")),
    ForgeProj("forgeNether", forgeNetherMinecraftVersion, forgeNetherVersion, true, listOf("forgeCliffs")),
    ForgeProj("forgePillage", forgePillageMinecraftVersion, forgePillageVersion, true, listOf("forgeNether")),
    ForgeProj("forgeAquatic", forgeAquaticMinecraftVersion, forgeAquaticVersion, true, listOf("forgeNether", "mc"), "20180921-1.13", "snapshot"),
)

val forgeEntrypoints = listOf(
    "forgeCavesEntrypoint",
    "forgePillageEntrypoint",
    "forgeAquaticEntrypoint"
)

val forgeSourceSets: Map<String, SourceSet> = forgeProjects.associate { p ->
    p.name to createSourceSet(p.name, "forge")
}

val forgeCompileOnly: Map<String, Configuration> = forgeProjects.associate { p ->
    p.name to getCompileOnly("${p.name}CompileOnly")
}

val forgeVersions: Map<SourceSet, ForgeProj> = forgeProjects.associate { p ->
    forgeSourceSets.getValue(p.name) to ForgeProj(p.name, p.minecraftVersion, p.loaderVersion, p.remap, p.deps, p.mcpVersion, p.mcpBuild)
}

val mc: SourceSet by sourceSets.creating

val extension: SourceSet by sourceSets.creating
val extensionCompileOnly: Configuration by configurations.getting

/* CompileOnly */
val mcCompileOnly: Configuration by configurations.getting
//configurations.compileOnly.get().extendsFrom(mcCompileOnly)

(fabricCompileOnly + bukkitCompileOnly + neoforgeCompileOnly).forEach {
    it.value.extendsFrom(configurations.compileOnly.get())
}

/* Implementation */
val mainImplementation: Configuration by configurations.creating {
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
    unimined.ornitheMaven()
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

fun sourceSetExists(sourceSetName: String): Boolean {
    val sourceSet = sourceSets.find {
        it.name == sourceSetName
    }

    return null != sourceSet && !sourceSet.compileClasspath.isEmpty && !sourceSet.runtimeClasspath.isEmpty
}

dependencies {
    configurations.compileOnly(libs.annotations)
    configurations.compileOnly(libs.mixin)

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

    neoforgeProjects.forEach { p ->
        p.deps.map { depName -> neoforgeSourceSets.getValue(depName) }.forEach { d ->
            if (d.name != p.name) {
                add(neoforgeCompileOnly.getValue(p.name).name, d.output)
            }
        }
    }

    // froge
    forgeSourceSets.forEach { (n, s) ->
        if (!n.endsWith("Entrypoint")) {
            for (e in forgeEntrypoints) {
                // entrypoint can access all
                if (e.endsWith("Entrypoint"))
                    add(forgeCompileOnly.getValue(e).name, s.output)
            }
        }
        if (n != "forgeCommon") {
            // all can access common
            add(forgeCompileOnly.getValue(n).name, forgeSourceSets.getValue("forgeCommon").output)
        }
    }

    forgeProjects.forEach { p ->
        p.deps.map { depName -> sourceSets[depName] }.forEach { d ->
            if (d.name != p.name) {
                add(forgeCompileOnly.getValue(p.name).name, d.output)
            }
        }
    }

//    listOf("forgeAquaticEntrypoint", "forgeAquatic").forEach {
//        add(it, mc.output);
//    }

    // universal deps
    (fabricCompileOnly + neoforgeCompileOnly + forgeCompileOnly + bukkitCompileOnly).forEach { (_, c) ->
        add(c.name, "com.vdurmont:semver4j:3.1.0")
        add(c.name, "org.apache.commons:commons-lang3:3.19.0")
        add(c.name, "commons-io:commons-io:2.20.0")
    }

    (bukkitCompileOnly).forEach { (_, c) ->
        add(c.name, mc.output)
    }

    extensionCompileOnly(sourceSets.main.get().output)
    extensionCompileOnly("club.minnced:discord-webhooks:0.8.4")
    extensionCompileOnly("net.dv8tion:JDA:6.1.0")
    extensionCompileOnly("com.vdurmont:semver4j:3.1.0")
    extensionCompileOnly("com.google.code.gson:gson:2.13.0")
    mcCompileOnly("com.vdurmont:semver4j:3.1.0")
    mcCompileOnly("org.bukkit:bukkit:$bukkitEmeraldMcVersion-$bukkitEmeraldVersion")

    // common deps
    implementation("com.google.code.gson:gson:2.13.0")
    implementation("org.danilopianini:gson-extras:3.3.0")
    implementation("club.minnced:discord-webhooks:0.8.4")
    implementation("net.dv8tion:JDA:6.1.0") {
        exclude(module = "opus-java")
    }
    implementation("me.scarsz.jdaappender:jda5:1.2.3")
//    implementation("org.slf4j:slf4j-jdk14:2.0.17")
    implementation("org.apache.logging.log4j:log4j-api:2.25.1")
    implementation("com.vdurmont:semver4j:3.1.0")
    implementation("org.apache.commons:commons-lang3:3.19.0")
    implementation("commons-io:commons-io:2.20.0")
}

/** Unimined */
unimined.footgunChecks = false

unimined.minecraft(mc) {
    combineWith(sourceSets.main.get())
    version(mcMinecraftVersion)
    mappings {
        parchment(mcParchmentMinecraft, mcParchmentVersion)
        mojmap()
    }

    defaultRemapJar = false
}

fabricVersions.forEach { it ->
    unimined.minecraft(it.key) {
        println("========== " + it.value.name + " ==========");

        combineWith(mc)
        version(it.value.minecraftVersion)

        mappings {
            if (it.value.parchmentMinecraft != null && it.value.parchmentVersion != null)
                parchment(it.value.parchmentMinecraft!!, it.value.parchmentVersion!!)

            mojmap()
        }

        fabric {
            loader(fabricLoaderVersion)
            if (it.key.name == "fabricEntrypoint")
                accessWidener(project.projectDir.resolve("src/entrypoint/fabricEntrypoint/resources/rrdiscordbridge.accesswidener"))
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
            "me.dexrn.rrdiscordbridge.mixins.vanilla.intermediary.${it.value.name.replaceFirstChar { c -> c.lowercase() }}"
        )
        relocate(
            "me.dexrn.rrdiscordbridge.impls.vanilla",
            "me.dexrn.rrdiscordbridge.impls.vanilla.intermediary.${it.value.name.replaceFirstChar { c -> c.lowercase() }}"
        )
        relocate(
            "me.dexrn.rrdiscordbridge.mc.impls.vanilla",
            "me.dexrn.rrdiscordbridge.mc.impls.vanilla.intermediary.${it.value.name.replaceFirstChar { c -> c.lowercase() }}"
        )
    }
}

forgeVersions.forEach { it ->
    println("========== " + it.value.name + " ==========");
    unimined.minecraft(it.key) {
        if (!it.value.mcpVersion.isEmpty()) {
            println("Using feather mappings")
            combineWith(sourceSets.main.get())
            mappings {
                searge()
                mcp(it.value.mcpBuild, it.value.mcpVersion)

//                ornitheGenVersion = 2;
//
//                searge()
//                calamus()
//                feather(4); //what the fuck is feather(4)
//                stub.withMappings("searge", "intermediary") {
//                    // METHODs net/minecraft/entity/item/EntityMinecart/[net/minecraft/entity/item/EntityMinecartFurnace/func_174898_m, getMaxSpeed]()D -> getMaxSpeed
//                    c(
//                        "apn",
//                        listOf(
//                            "net/minecraft/entity/item/EntityMinecartFurnace",
//                        )
//                    ) {
//                        m("getMaxSpeed", "()D", "func_174898_m", "getMaxSpeedFeather")
//                    }
//                }
            }
        } else {
            combineWith(mc)
            mappings {
                mojmap()
            }
        }

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

    if (it.value.remap) {
        tasks.register<ShadowJar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar") {
            dependsOn("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
            from(zipTree(tasks.getByName<Jar>("remap${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile))
            archiveClassifier.set("${it.key.name}-relocated")
            destinationDirectory.set(layout.buildDirectory.dir("tmp/forge/relocated"))
            relocate(
                "me.dexrn.rrdiscordbridge.mixins.vanilla",
                "me.dexrn.rrdiscordbridge.mixins.vanilla.srg.${it.value.name.replaceFirstChar { c -> c.lowercase() }}"
            )
            relocate(
                "me.dexrn.rrdiscordbridge.impls.vanilla",
                "me.dexrn.rrdiscordbridge.impls.vanilla.srg.${it.value.name.replaceFirstChar { c -> c.lowercase() }}"
            )
            relocate(
                "me.dexrn.rrdiscordbridge.mc.impls.vanilla",
                "me.dexrn.rrdiscordbridge.mc.impls.vanilla.srg.${it.value.name.replaceFirstChar { c -> c.lowercase() }}"
            )
        }
    }
}

neoforgeVersions.forEach { it ->
    unimined.minecraft(it.key) {
        println("========== " + it.value.name + " ==========");

        combineWith(mc)
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
//        combineWith(mc)
////        defaultRemapJar = true
//    }

    tasks.register<Jar>("${set.value.name}Jar") {
        println("Registering ${set.value.name}")

        from(mc.output)

//        group = JavaBasePlugin.BUILD_TASK_NAME
//        archiveBaseName.set(set.value.jarName)

        destinationDirectory.set(layout.buildDirectory.dir("tmp/bukkit"))

        dependsOn("${set.value.name}ShadowJar")
    }
}

tasks.withType<RemapJarTask> {
    mixinRemap {
        enableBaseMixin()
        disableRefmap()
    }
}

/** Main */
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.register("buildMod") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    dependsOn("moddedShadowJar")
}

tasks.named("build").configure {
    dependsOn("buildMod")
}

tasks.register<Jar>("buildMain") {
    group = JavaBasePlugin.BUILD_TASK_NAME
    dependsOn(mainShadowJar)
}

tasks.named<Jar>("jar").configure {
    destinationDirectory.set(layout.buildDirectory.dir("tmp/rrdb"))
}

tasks.withType<JavaCompile>().configureEach {
    options.isFork = true
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.withType<ProcessResources> {
    filesMatching(
        listOf(
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
        )
    ) {
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
    "META-INF/maven/**",
//    "META-INF/versions/**"
)

val mainShadowJar = tasks.register<ShadowJar>("mainShadowJar") {
    archiveClassifier = ""

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

    from(sourceSets.main.get().output, extension.output)

    minimize()

    from(listOf("README.md", "LICENSE")) {
        into("META-INF")
    }
}

tasks.register<ShadowJar>("moddedShadowJar") {
    archiveClassifier = "AIO" // welcome back terraria aio worlds

    dependsOn(mainShadowJar)
    configurations = listOf(project.configurations.getByName("runtimeClasspath"))

    fabricVersions.forEach {
        dependsOn("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
    }
    forgeVersions.forEach {
        if (it.value.remap)
            dependsOn("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar")
    }
    from(
        mc.output, extension.output, sourceSets.main.get().output,
        fabricVersions.map {
            println("Shadowing ${it.key.name}")
            zipTree(tasks.getByName<Jar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile)
        },
        forgeVersions.map {
            println("Shadowing ${it.key.name}")
            if (!it.value.remap)
                it.key.output
            else
                zipTree(tasks.getByName<Jar>("relocate${it.key.name.replaceFirstChar { c -> c.uppercase() }}Jar").archiveFile.get().asFile)
        },
        neoforgeSourceSets.map {
            println("Shadowing ${it.key}")
            it.value.output
        },
        bukkitSourceSets.map {
            println("Shadowing ${it.key}")
            it.value.output
        },
    )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    minimize()

    relocate("org.slf4j", "rrdb_reloc.org.slf4j")
    relocate("gnu", "rrdb_reloc.gnu")

    relocate(
        "kotlin",
        "rrdb_reloc.kotlin"
    )

    relocate(
        "org.apache.commons",
        "rrdb_reloc.org.apache.commons"
    )

//    relocate(
//        "org.apache.logging",
//        "rrdb_reloc.org.apache.logging"
//    )

    relocate(
        "com.fasterxml.jackson",
        "rrdb_reloc.com.fasterxml.jackson"
    )

    relocate(
        "com.google.gson",
        "rrdb_reloc.com.google.gson"
    )

    exclude(ex)
    exclude(
        listOf(
            "org/bukkit/**",
//            "com/google/gson/**",
//            "module-info.class",
            "META-INF/versions/9/module-info.class",
            "META-INF/versions/9/org/apache/logging/**",
            "org/apache/logging/**",
            "com/google/errorprone/**",
//            "com/fasterxml/jackson/**",
            "META-INF/services/com.fasterxml.jackson.core.JsonFactory/**",
            "META-INF/services/com.fasterxml.jackson.core.ObjectCodec/**",
//            "META-INF/services/org.slf4j.spi.SLF4JServiceProvider/**",
            "javax/annotation/**",
//            "org/slf4j/**"
        )
    )

}

/** Extra */
tasks.build.get().dependsOn("spotlessApply")

tasks.register<Javadoc>("genJavadoc") {
    group = JavaBasePlugin.DOCUMENTATION_GROUP

    source = sourceSets.main.get().allJava
    classpath = files(sourceSets.main.get().output, sourceSets.main.get().compileClasspath, extension.output)

    setDestinationDir(file("javadoc/generated"))

    val css = file("javadoc/javadoc.css")

    if (css.exists())
        options {
            this as StandardJavadocDocletOptions
            stylesheetFile = css
        }
}
