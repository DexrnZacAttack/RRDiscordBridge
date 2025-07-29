# Retro-Release Discord Bridge
[![](https://dcbadge.limes.pink/api/server/k2wGKEaCRA)](https://discord.gg/k2wGKEaCRA)   

Discord bridge/relay plugin that [supports old Minecraft versions and server software](#tested-versions) (Beta 1.4-Release 1.21.1)

For config info, check out [this page](https://rrdiscordbridge.nostalgica.net/javadoc/io/github/dexrnzacattack/rrdiscordbridge/config/Settings.html#field-summary).

Download: https://modrinth.com/plugin/rrdiscordbridge

## Features
- Message Relay
- [Version support](#tested-versions)
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
  - Server Stop
  - /say
  - /me
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
- Chat Extensions (configurable)
  - **Extra info available in-game by typing `/cext help`**.
  - Waypoint embed
    - Allows for Xaero's Minimap and JourneyMap/VoxelMap waypoints to be embedded in the relay channel.
  - Operator Chat
    - Allows for communicating between ops and optionally a (likely private) discord channel.
    - Inspired by MCGalaxy's OPChat feature.
    - Syntax: ## \<msg\>
- Customizable [color palette](https://rrdiscordbridge.nostalgica.net/javadoc/io/github/dexrnzacattack/rrdiscordbridge/config/ColorPalette.html#field-summary) for messages sent to Discord

## Supported versions
### CraftBukkit (and forks)
  - [X] [UberBukkit](https://github.com/Moresteck/uberbukkit)
  - [ ] b1.3_01-
  - [X] b1.4-r1.0
  - [X] [Project Poseidon](https://github.com/retromcorg/Project-Poseidon)
  - [X] 1.1-1.2.5
  - [X] 1.3.1-1.7.8
  - [X] 1.7.9-1.11.2
  - [X] 1.12-1.19.1
  - [X] 1.19.2+
### Fabric
  - [ ] 1.14.4
  - [ ] 1.15.2
  - [X] 1.16.4-1.18.2
  - [X] 1.19
  - [X] 1.19.1-1.19.2
  - [X] 1.19.3-1.20.1
  - [X] 1.20.2-1.21.8
### NeoForge
  - [ ] 1.20.2-1.20.4
    - `neoforge.mods.toml` was unused until 1.20.5 
  - [X] 1.20.5-1.21.8
## Credits
- [p0t4t0sandwich](https://github.com/p0t4t0sandwich)
  - Was very helpful in helping me set up gradle and helped me through every issue I had 