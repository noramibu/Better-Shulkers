<img src="fabric/src/main/resources/assets/bettershulkers/icon.png" width="128" height="128">

# Better Shulkers
[![Modrinth](https://img.shields.io/badge/Modrinth-Download-green?logo=modrinth)](https://modrinth.com/mod/better-shulkers)
[![CurseForge](https://img.shields.io/badge/CurseForge-Download-orange?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/better-shulkers)

[![Available for Fabric](https://img.shields.io/badge/Available%20for-Fabric-5C5240?&logo=fabric&logoColor=white)](https://modrinth.com/mod/better-shulkers/versions?l=fabric)
[![Available for Quilt](https://img.shields.io/badge/Available%20for-Quilt-5A2C91?logo=quilt&logoColor=white)](https://modrinth.com/mod/better-shulkers/versions?l=quilt)
[![Available for NeoForge](https://img.shields.io/badge/Available%20for-NeoForge-FF6600?logo=neoforge&logoColor=white)](https://modrinth.com/mod/better-shulkers/versions?l=neoforge)
- --
## About
Better Shulkers is an open-source Fabric and NeoForge Minecraft mod that works completely on the server (no client mod needed)
 and improves many aspects of shulker boxes. This is intended to be used on vanilla-compatible servers, however should work
 fine with modded content.

### This is Tater Certified's entry in the [2025 NeoForge Server-side Summer](https://neoforged.net/news/2025serversidesummer/) competition!

## Features
This is a brief list of features. More can be found on [our website](https://noramibu.github.io/Better-Shulkers/):
- Open shulker boxes from your hotbar
- Automatically pick up items and place them in a shulker box
- Whitelist certain items for a shulker box
- Visual display to show shulker box whitelists

## Downloads
Download Better Shulkers from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/better-shulkers) or [Modrinth](https://modrinth.com/mod/better-shulkers).
The mod is compatible with Minecraft 1.21 and above.

## Showcase Video
<a href="http://www.youtube.com/watch?feature=player_embedded&v=HpEtTcU-fMk" target="_blank">
 <img src="http://img.youtube.com/vi/HpEtTcU-fMk/mqdefault.jpg" alt="" width="1920" height="1080" border="10" />
</a>

## Configuration & Permissions

### Configuration Options
The mod creates a `bettershulkers.toml` file in your server's config folder. Here are all available options:

| Option                                            | Default  | Description                                                                                                                                                            |
|---------------------------------------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `disable-pickup-feature-of-shulkers`              | `false`  | Disables the main feature of this mod, which is shulkers retaining their inventory when broken. **Note:** This does NOT disable the recipe to craft pickable shulkers. |
| `add-recipe-for-pickable-shulker`                 | `true`   | If true, a recipe is added to make shulkers pickable. **Note:** This does NOT disable the pickup feature, only the recipe to craft them.                               |
| `right-click-to-open-shulker`                     | `true`   | If true, players can open shulker boxes by right-clicking them in hand.                                                                                                |
| `show-material-display`                           | `true`   | If true, item displays will render on the shulker box lid to show the material. **Note:** Displays may appear slow if players have high latency.                       |
| `require-permission-for-command`                  | `true`   | If true, players will need permissions to use the `/shulker` command.                                                                                                  |
| `require-permission-for-right-click-open-shulker` | `false`  | If true, players will need 'bettershulkers.open' permission to open shulker boxes by right-clicking them.                                                              |

### Permission Nodes
The following permission nodes are available for server administrators:

| Permission Node                 | Description                                                                                                  |
|---------------------------------|--------------------------------------------------------------------------------------------------------------|
| `bettershulkers.command.set`    | Allows setting a shulker's material using the `/shulker` command.                                            |
| `bettershulkers.command.reload` | Allows reloading the configuration file using the `/shulker` command.                                        |
| `bettershulkers.open`           | Allows opening shulker boxes by right-clicking them in hand. (permission req. for this disabled as default)  |

### Commands
- `/shulker set <material>` - Set a shulker box's material (requires `bettershulkers.command.set` permission)
- `/shulker reload` - Reload the configuration file (requires `bettershulkers.command.reload` permission)

## Need Help?
Just join our Discord server!<p>
[![Join our Discord](https://img.shields.io/discord/948704397569958038.svg?label=Join%20us%20on%20Discord&logo=discord&style=for-the-badge)](https://discord.gg/XGw3Te7QYr)

## Credits
|                                                                                                                                                                       | Name            | Role                      |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|:--------------------------|
| [![noramibu's Avatar](https://avatars.githubusercontent.com/u/50046813?s=48)](https://github.com/noramibu)                                                            | noramibu         | Developer                 |
| [![QPCrummer's Avatar](https://avatars.githubusercontent.com/u/66036033?s=48)](https://github.com/QPCrummer)                                                          | qpcrummer       | Developer                 |
| [![TaterCertifed's Avatar](https://avatars.githubusercontent.com/u/98563278?s=48&u=8a1ddaf201e7c943713e4aee471ad1aa0fbe682f&v=4)](https://github.com/Tater-Certified) | Tater Certified | Contributing Organization |
