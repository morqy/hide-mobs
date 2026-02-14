# DeathInvis

A client-side Fabric mod that hides mob death animations, making them instantly disappear when killed. No more watching mobs slowly tip over — they just vanish.

## What it does

- **Hides death animations** — when you kill a mob, it disappears immediately instead of playing the fall-over animation
- **Suppresses death particles and sounds** — no poof cloud or death sound effects for hidden mobs
- **Only affects mobs you attacked** — AoE/splash damage won't hide bystander mobs, only the one you directly hit
- **Hidden mobs are non-blocking** — you can hit entities and mine blocks through hidden mobs as if they weren't there
- **Purely client-side** — the server doesn't know the mod exists; nothing is modified server-side

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.18.4+
- Fabric API

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) and place it in your `mods` folder
3. Place the DeathInvis `.jar` in your `mods` folder
4. Launch the game

## Commands

All commands are client-side and start with `/di`:

| Command | Description |
|---------|-------------|
| `/di` | Show help |
| `/di help` | Show help |
| `/di toggle` | Enable or disable the mod |
| `/di mode` | Show current hide mode |
| `/di mode death` | Set mode to hide on death (default) |
| `/di mode hit` | On-hit mode (currently not working) |

Commands support tab-completion and appear in chat history.

## Known Issues

- **On-hit mode does not work.** The `death` mode (hide on kill) is the only functional mode. On-hit mode is present in the code but disabled until a proper implementation is ready.

## Disclaimer

**Use at your own risk.** This mod modifies client-side rendering behavior. While it does not send any modified data to the server, using any client-side mod may violate the terms of service of certain servers. The author(s) take no responsibility for any consequences resulting from the use of this mod, including but not limited to account bans or restrictions.
