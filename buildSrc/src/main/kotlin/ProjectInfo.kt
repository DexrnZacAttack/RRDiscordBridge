import org.gradle.api.Project

object ProjectInfo

val Project.author: String get() = properties["author"].toString()
val Project.modName: String get() = properties["mod_name"].toString()
val Project.modId: String get() = properties["mod_id"].toString()
val Project.description: String get() = properties["description"].toString()
val Project.license: String get() = properties["license"].toString()

val Project.homepageUrl: String get() = properties["homepage_url"].toString()
val Project.issueUrl: String get() = properties["issue_url"].toString()
val Project.sourceUrl: String get() = properties["source_url"].toString()

val Project.minecraftVersion: String get() = properties["minecraft_version"].toString()
val Project.fabric1201MinecraftVersion: String get() = properties["fabric1201_minecraft_version"].toString()
val Project.parchmentMinecraft: String get() = properties["parchment_minecraft"].toString()
val Project.parchmentVersion: String get() = properties["parchment_version"].toString()
val Project.fabric1201ParchmentMinecraft: String get() = properties["fabric1201_parchment_minecraft"].toString()
val Project.fabric1201ParchmentVersion: String get() = properties["fabric1201_parchment_version"].toString()
val Project.fabric1201Version: String get() = properties["fabric1201_version"].toString()

val Project.bungeecordVersion: String get() = properties["bungeecord_version"].toString()
val Project.fabricVersion: String get() = properties["fabric_version"].toString()
val Project.fabricLoaderVersion: String get() = properties["fabric_loader_version"].toString()
val Project.forgeVersion: String get() = properties["forge_version"].toString()
val Project.neoForgeVersion: String get() = properties["neoforge_version"].toString()
val Project.paperVersion: String get() = properties["paper_version"].toString()

val Project.poseidonVersion: String get() = properties["poseidon_version"].toString()

val Project.bukkit10Version: String get() = properties["bukkit10_version"].toString()
val Project.bukkit10McVersion: String get() = properties["bukkit10_mc_version"].toString()

val Project.bukkit11Version: String get() = properties["bukkit11_version"].toString()
val Project.bukkit11McVersion: String get() = properties["bukkit11_mc_version"].toString()

val Project.bukkit13Version: String get() = properties["bukkit13_version"].toString()
val Project.bukkit13McVersion: String get() = properties["bukkit13_mc_version"].toString()

val Project.bukkit17Version: String get() = properties["bukkit17_version"].toString()
val Project.bukkit17McVersion: String get() = properties["bukkit17_mc_version"].toString()

val Project.spongeVersion: String get() = properties["sponge_version"].toString()
val Project.velocityVersion: String get() = properties["velocity_version"].toString()

val Project.javaVersion: String get() = properties["java_version"].toString()
