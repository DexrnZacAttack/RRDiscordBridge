# Retro-Release Discord Bridge
[![](https://dcbadge.limes.pink/api/server/k2wGKEaCRA)](https://discord.gg/k2wGKEaCRA)   

Discord bridge/relay plugin for both [old and new release (plus some Beta) versions](#tested-versions) (b1.7.3*-1.21.1) of Minecraft.   

For config info, check out [this](https://github.com/DexrnZacAttack/RRDiscordBridge/wiki/Config).

Download: https://modrinth.com/plugin/rrdiscordbridge   
Note: I am not a java developer, although I was able to find my way around this pretty easily.

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

## Tested versions
- [X] a1.2.6-b1.7.2 (using [UberBukkit](https://github.com/Moresteck/uberbukkit))
  - NOTE: UberBukkit is a fork of Project Poseidon (which is b1.7.3) with added support for older clients 
- [X] b1.7.3 (using [Project Poseidon](https://github.com/retromcorg/Project-Poseidon)/[UberBukkit](https://github.com/Moresteck/uberbukkit))
- [ ] b1.8.1
- [ ] 1.0   
- [X] 1.1
- [X] 1.2.5
- [X] 1.3.2
- [X] 1.4.7
- [X] 1.5.2
- [X] 1.6.4
- [X] 1.7.2
- [X] 1.8.8
- [X] 1.9.4
- [X] 1.10.2
- [X] 1.14.4
- [X] 1.15.2
- [X] 1.16.5
- [X] 1.17.1
- [X] 1.18.2
- [X] 1.21.1
- [X] 1.21.4
