import org.gradle.api.Project

object ProjectInfo

val Project.author: String get() = properties["author"].toString()
val Project.modName: String get() = properties["mod_name"].toString()
val Project.modId: String get() = properties["mod_id"].toString()
val Project.description: String get() = properties["description"].toString()
val Project.license: String get() = properties["license"].toString()
val Project.logo: String get() = properties["logo"].toString()

val Project.homepageUrl: String get() = properties["homepage_url"].toString()
val Project.issueUrl: String get() = properties["issue_url"].toString()
val Project.sourceUrl: String get() = properties["source_url"].toString()
val Project.modrinthUrl: String get() = properties["modrinth_url"].toString()
val Project.discordUrl: String get() = properties["discord_url"].toString()


val Project.minecraftVersion: String get() = properties["minecraft_version"].toString()
val Project.parchmentMinecraft: String get() = properties["parchment_minecraft"].toString()
val Project.parchmentVersion: String get() = properties["parchment_version"].toString()
val Project.fabricNetherParchmentMinecraft: String get() = properties["fabric_nether_parchment_minecraft"].toString()
val Project.fabricNetherParchmentVersion: String get() = properties["fabric_nether_parchment_version"].toString()
val Project.fabricNetherMinecraftVersion: String get() = properties["fabric_nether_minecraft_version"].toString()
val Project.fabricNetherVersion: String get() = properties["fabric_nether_version"].toString()
val Project.fabricWildParchmentMinecraft: String get() = properties["fabric_wild_parchment_minecraft"].toString()
val Project.fabricWildParchmentVersion: String get() = properties["fabric_wild_parchment_version"].toString()
val Project.fabricWildMinecraftVersion: String get() = properties["fabric_wild_minecraft_version"].toString()
val Project.fabricWildVersion: String get() = properties["fabric_wild_version"].toString()
val Project.fabricAllayParchmentMinecraft: String get() = properties["fabric_allay_parchment_minecraft"].toString()
val Project.fabricAllayParchmentVersion: String get() = properties["fabric_allay_parchment_version"].toString()
val Project.fabricAllayMinecraftVersion: String get() = properties["fabric_allay_minecraft_version"].toString()
val Project.fabricAllayVersion: String get() = properties["fabric_allay_version"].toString()
val Project.fabricVexParchmentMinecraft: String get() = properties["fabric_vex_parchment_minecraft"].toString()
val Project.fabricVexParchmentVersion: String get() = properties["fabric_vex_parchment_version"].toString()
val Project.fabricVexMinecraftVersion: String get() = properties["fabric_vex_minecraft_version"].toString()
val Project.fabricVexVersion: String get() = properties["fabric_vex_version"].toString()

val Project.fabricVersion: String get() = properties["fabric_version"].toString()
val Project.fabricLoaderVersion: String get() = properties["fabric_loader_version"].toString()
val Project.forgePotMinecraftVersion: String get() = properties["forge_pot_minecraft_version"].toString()
val Project.forgePotVersion: String get() = properties["forge_pot_version"].toString()
val Project.forgeTradeMinecraftVersion: String get() = properties["forge_trade_minecraft_version"].toString()
val Project.forgeTradeVersion: String get() = properties["forge_trade_version"].toString()
val Project.forgeTrailsMinecraftVersion: String get() = properties["forge_trails_minecraft_version"].toString()
val Project.forgeTrailsVersion: String get() = properties["forge_trails_version"].toString()
val Project.neoforgePotMinecraftVersion: String get() = properties["neoforge_pot_minecraft_version"].toString()
val Project.neoforgePotVersion: String get() = properties["neoforge_pot_version"].toString()
val Project.neoforgeTradeMinecraftVersion: String get() = properties["neoforge_trade_minecraft_version"].toString()
val Project.neoforgeTradeVersion: String get() = properties["neoforge_trade_version"].toString()
val Project.neoForgeVersion: String get() = properties["neoforge_version"].toString()

val Project.useLocalBukkitCake: Boolean get() = properties["use_local_bukkit_cake"].toString().toBoolean() // do I need to do toString?
val Project.poseidonVersion: String get() = properties["poseidon_version"].toString()

val Project.bukkitCookieVersion: String get() = properties["bukkit_cookie_version"].toString()
val Project.bukkitCookieMcVersion: String get() = properties["bukkit_cookie_mc_version"].toString()

val Project.bukkitFlatVersion: String get() = properties["bukkit_flat_version"].toString()
val Project.bukkitFlatMcVersion: String get() = properties["bukkit_flat_mc_version"].toString()

val Project.bukkitEmeraldVersion: String get() = properties["bukkit_emerald_version"].toString()
val Project.bukkitEmeraldMcVersion: String get() = properties["bukkit_emerald_mc_version"].toString()

val Project.bukkitRealmsVersion: String get() = properties["bukkit_realms_version"].toString()
val Project.bukkitRealmsMcVersion: String get() = properties["bukkit_realms_mc_version"].toString()

val Project.bukkitColorVersion: String get() = properties["bukkit_color_version"].toString()
val Project.bukkitColorMcVersion: String get() = properties["bukkit_color_mc_version"].toString()

val Project.bukkitVexVersion: String get() = properties["bukkit_vex_version"].toString()
val Project.bukkitVexMcVersion: String get() = properties["bukkit_vex_mc_version"].toString()

val Project.javaVersion: String get() = properties["java_version"].toString()
