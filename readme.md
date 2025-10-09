# Retro-Release Discord Bridge
[![Download](https://img.shields.io/modrinth/v/rrdiscordbridge?logo=modrinth&label=Modrinth&color=0bc95a)](https://modrinth.com/plugin/RRDiscordBridge)
[![Docs](https://img.shields.io/badge/Read%20Documentation-4e5151?logo=gitbook)](https://rrdiscordbridge.nostalgica.net/javadocs)
[![Discord](https://dcbadge.limes.pink/api/server/k2wGKEaCRA)](https://discord.gg/k2wGKEaCRA)

Discord bridge/relay plugin that [supports old Minecraft versions and server software](#tested-versions) (Beta 1.4-Release 1.21.1)

For config info, check out [this page](https://rrdiscordbridge.nostalgica.net/javadocs/io/github/dexrnzacattack/rrdiscordbridge/config/Settings.html#field-summary).

Download: https://modrinth.com/plugin/rrdiscordbridge

When reading the source code, you may notice that the projects have odd names, this is to make the source look slightly cleaner.
See [versions.md](/versions.md) for more info.

## Features
- Message Relay
- [Version support](#supported-versions)
- Broadcasting (/dcbroadcast)
- Server Stats from Discord (/about)
- Relays the following to Discord (configurable)
  - Player Join
  - Player Leave
  - Player Kick
  - Player Death
  - Player Chat
  - Player Advancements/Achievements
    - Contains advancement description (if applicable) and link to wiki page 
  - Server Start
  - Plugin Reload
  - Server Stop
  - /say
  - /me
  - Server Console (if configured)
- Relays the following information from Discord (configurable)
  - Channel message
  - User join (if the relay channel is also the system messages channel)
  - Server boost (if the relay channel is also the system messages channel)
  - Thread creation
  - Message pin
  - Poll created/ended (with results)
  - Slash commands used
  - User app used (also activities)
  - Message forwarded (probably not complete)
- Extensions (configurable)
  - **Extra info available in-game by typing `/rdbext help`**.
  - Built-in extensions:
    - Waypoint embed (Waypoints)
      - Allows for Xaero's Minimap and JourneyMap/VoxelMap waypoints to be embedded in the relay channel.
      - ### [See config options](https://rrdiscordbridge.nostalgica.net/javadocs/io/github/dexrnzacattack/rrdiscordbridge/extension/extensions/options/WaypointExtensionOptions.html#field-summary)

    - Operator Chat (OpChat)
      - Allows for communicating between ops and optionally a (likely private) discord channel.
      - Inspired by MCGalaxy's OPChat feature.
      - Syntax: ## \<msg\>
      - ### [See config options](https://rrdiscordbridge.nostalgica.net/javadocs/io/github/dexrnzacattack/rrdiscordbridge/extension/extensions/options/OpChatExtensionOptions.html#field-summary)
  - ### For development info, see [the example repo](https://github.com/Nostalgica-Reverie/RRDiscordBridgeExampleExtension)
- Customizable [color palette](https://rrdiscordbridge.nostalgica.net/javadocs/io/github/dexrnzacattack/rrdiscordbridge/config/ColorPalette.html#field-summary) for messages sent to Discord

## Supported versions
### CraftBukkit (and forks)
  - [X] [UberBukkit](https://github.com/Moresteck/uberbukkit)
  - [X] b1.2_01-b1.3_01 (int. `bukkitCake` | Made for Bukkit Build 33)
    - Missing many features, as build 33 was missing many events.
    - No commands
  - [X] b1.4-r1.0 (int. `bukkitCookie`)
  - [X] [Project Poseidon](https://github.com/retromcorg/Project-Poseidon)
  - [X] 1.1-1.2.5 (int. `bukkitFlat`)
  - [X] 1.3.1-1.7.8 (int. `bukkitEmerald`)
  - [X] 1.7.9-1.11.2 (int. `bukkitRealms`)
  - [X] 1.12-1.19.1 (int. `bukkitColor`)
  - [X] 1.19.2+ (int. `bukkitVex`)
### Fabric
  - [ ] 1.14.4
  - [ ] 1.15.2
  - [X] 1.16.4-1.18.2
  - [X] 1.19
  - [X] 1.19.1-1.19.2
  - [X] 1.19.3-1.20.1
  - [X] 1.20.2-1.21.8
### NeoForge
  - [X] 1.20.2-1.20.4
    - `neoforge.mods.toml` was unused until 1.20.5 
  - [X] 1.20.5-1.21.8
### Forge
  - [ ] 1.20.2
  - [X] 1.20.3-1.21.8
### Ornithe
  - Coming soon (hopefully)

## Credits
- [placeholder.co](https://placeholder.co)
  - Used for waypoint badges 
- [MCHeads](https://www.mc-heads.net/)
  - Used for player head avatar 
- [p0t4t0sandwich](https://github.com/p0t4t0sandwich)
  - Was very helpful in helping me set up gradle and helped me through every issue I had