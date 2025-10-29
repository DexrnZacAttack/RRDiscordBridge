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

val Project.mcParchmentMinecraft: String get() = properties["mc_parchment_minecraft"].toString()
val Project.mcParchmentVersion: String get() = properties["mc_parchment_version"].toString()
val Project.mcMinecraftVersion: String get() = properties["mc_minecraft_version"].toString()

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

val Project.fabricTradeParchmentMinecraft: String get() = properties["fabric_trade_parchment_minecraft"].toString()
val Project.fabricTradeParchmentVersion: String get() = properties["fabric_trade_parchment_version"].toString()
val Project.fabricTradeMinecraftVersion: String get() = properties["fabric_trade_minecraft_version"].toString()
val Project.fabricTradeVersion: String get() = properties["fabric_trade_version"].toString()

val Project.fabricPotParchmentMinecraft: String get() = properties["fabric_pot_parchment_minecraft"].toString()
val Project.fabricPotParchmentVersion: String get() = properties["fabric_pot_parchment_version"].toString()
val Project.fabricPotMinecraftVersion: String get() = properties["fabric_pot_minecraft_version"].toString()
val Project.fabricPotVersion: String get() = properties["fabric_pot_version"].toString()

val Project.fabricPawsMinecraftVersion: String get() = properties["fabric_paws_minecraft_version"].toString()
val Project.fabricPawsVersion: String get() = properties["fabric_paws_version"].toString()

val Project.fabricCopperParchmentMinecraft: String get() = properties["fabric_copper_parchment_minecraft"].toString()
val Project.fabricCopperParchmentVersion: String get() = properties["fabric_copper_parchment_version"].toString()
val Project.fabricCopperMinecraftVersion: String get() = properties["fabric_copper_minecraft_version"].toString()
val Project.fabricCopperVersion: String get() = properties["fabric_copper_version"].toString()

val Project.fabricLoaderVersion: String get() = properties["fabric_loader_version"].toString()
val Project.forgeCopperMinecraftVersion: String get() = properties["forge_copper_minecraft_version"].toString()
val Project.forgeCopperVersion: String get() = properties["forge_copper_version"].toString()
val Project.forgeSkiesMinecraftVersion: String get() = properties["forge_skies_minecraft_version"].toString()
val Project.forgeSkiesVersion: String get() = properties["forge_skies_version"].toString()
val Project.forgePawsMinecraftVersion: String get() = properties["forge_paws_minecraft_version"].toString()
val Project.forgePawsVersion: String get() = properties["forge_paws_version"].toString()
val Project.forgePotMinecraftVersion: String get() = properties["forge_pot_minecraft_version"].toString()
val Project.forgePotVersion: String get() = properties["forge_pot_version"].toString()
val Project.forgeTradeMinecraftVersion: String get() = properties["forge_trade_minecraft_version"].toString()
val Project.forgeTradeVersion: String get() = properties["forge_trade_version"].toString()
val Project.forgeAllayMinecraftVersion: String get() = properties["forge_allay_minecraft_version"].toString()
val Project.forgeAllayVersion: String get() = properties["forge_allay_version"].toString()
val Project.neoforgeCopperMinecraftVersion: String get() = properties["neoforge_copper_minecraft_version"].toString()
val Project.neoforgeCopperVersion: String get() = properties["neoforge_copper_version"].toString()
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
