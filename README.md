<img src="fabric/src/main/resources/assets/bettershulkers/icon.png" width="128" height="128">

# Better Shulkers
[![Modrinth](https://img.shields.io/badge/Modrinth-Download-green?logo=modrinth)](https://modrinth.com/mod/better-shulkers)
[![CurseForge](https://img.shields.io/badge/CurseForge-Download-orange?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/better-shulkers)

[![Available for Fabric](https://img.shields.io/badge/Available%20for-Fabric-5C5240?&logo=fabric&logoColor=white)](https://modrinth.com/mod/better-shulkers/versions?l=fabric)
[![Available for Quilt](https://img.shields.io/badge/Available%20for-Quilt-5A2C91?logo=quilt&logoColor=white)](https://modrinth.com/mod/better-shulkers/versions?l=quilt)
[![Available for NeoForge](https://img.shields.io/badge/Available%20for-NeoForge-FF6600?logo=neoforge&logoColor=white)](https://modrinth.com/mod/better-shulkers/versions?l=neoforge)
- --
## About
Better Shulkers is an open-source Minecraft mod that enhances shulker boxes with powerful automation features. The mod works completely on the server side, requiring no client modifications, making it perfect for vanilla-compatible servers while also working seamlessly with modded content.

### Key Benefits
- **Server-side only** - No client mod required, works with vanilla clients
- **Vanilla compatible** - Players don't need to install anything
- **Multiple platforms** - Available for Fabric, Quilt, and NeoForge
- **Configurable** - Extensive configuration options for server administrators
- **Permission system** - Granular permission control for different features

### This is Tater Certified's entry in the [2025 NeoForge Server-side Summer](https://neoforged.net/news/2025serversidesummer/) competition!

## Features
This is a brief list of features. More can be found on [our website](https://noramibu.github.io/Better-Shulkers/):
- **Open shulker boxes from anywhere** - Right-click shulker boxes in your hand or inventory to open them without placing them
- **Automatic item pickup** - Shulker boxes can automatically pick up items that match their assigned material
- **Material assignment** - Assign materials to shulker boxes using either crafting recipes or enchantments
- **Visual material display** - See what material a shulker box is assigned to via visual displays on the box
- **Server-side only** - Works completely on the server with no client mod required
- **Vanilla compatible** - Works with vanilla Minecraft clients

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

| Option                                            | Default       | Description                                                                                                                                                                                          |
|---------------------------------------------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `item-pickup-type`                                | `ENCHANTMENT` | Controls how shulker boxes can pick up items automatically:<br>• `NONE`: Shulker boxes do not automatically pick up items<br>• `RECIPE`: All shulkers with a material can pick up items (uses crafting recipe)<br>• `ENCHANTMENT`: Only shulkers with a material and the Material Collector enchantment can pick up items |
| `open-from-inventory`                             | `true`        | If true, players can open shulker boxes by right-clicking them in their hand or in their inventory                                                                                                   |
| `show-material-display`                           | `true`        | If true, item displays will render on the shulker box lid to show the material. **Note:** Displays may appear slow if players have high latency.                                                     |
| `require-permission-for-command`                  | `true`        | If true, players will need permissions to use the `/shulker` command.                                                                                                                                |
| `require-permission-for-right-click-open-shulker` | `false`       | If true, players will need 'bettershulkers.open' permission to open shulker boxes by right-clicking them.                                                                                            |

### How It Works

#### Material Assignment Methods

**Recipe Method (`item-pickup-type = "RECIPE"`):**
- Place a shulker box in the center of a crafting table
- Surround it with 4 obsidian blocks in the corners
- Place the desired material item in the remaining 4 slots
- This creates a shulker box that will automatically pick up that material

**Enchantment Method (`item-pickup-type = "ENCHANTMENT"`):**
- First, obtain the "Material Collector" enchantment through these methods:
  - **Trading:** Librarian villagers may offer enchanted books with this enchantment
  - **Loot:** Found in various loot chests throughout the world
- Apply the enchantment to a shulker box using an anvil
- Then use the anvil to combine the enchanted shulker with the desired material item
- This creates a shulker box that will automatically pick up that material

### Permission Nodes
The following permission nodes are available for server administrators:

| Permission Node                 | Description                                                                                                  |
|---------------------------------|--------------------------------------------------------------------------------------------------------------|
| `bettershulkers.command.set`    | Allows setting a shulker's material using the `/shulker set <material>` command.                                            |
| `bettershulkers.command.reload` | Allows reloading the configuration file using the `/shulker reload` command.                                        |
| `bettershulkers.open`           | Allows opening shulker boxes by right-clicking them in hand. (permission requirement for this is disabled by default)  |

### Commands
- `/shulker set <material>` - Set a shulker box's material (requires `bettershulkers.command.set` permission)
- `/shulker reload` - Reload the configuration file (requires `bettershulkers.command.reload` permission)

### Compatibility
- **Minecraft Version:** 1.21 and above
- **Loaders:** Fabric, Quilt, and NeoForge
- **Server-side only:** No client mod required, works with vanilla clients

## Need Help?
Just join our Discord server!<p>
[![Join our Discord](https://img.shields.io/discord/948704397569958038.svg?label=Join%20us%20on%20Discord&logo=discord&style=for-the-badge)](https://discord.gg/XGw3Te7QYr)

## Credits
|                                                                                                                                                                       | Name            | Role                      |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|:----------------|:--------------------------|
| [![noramibu's Avatar](https://avatars.githubusercontent.com/u/50046813?s=48)](https://github.com/noramibu)                                                            | noramibu         | Developer                 |
| [![QPCrummer's Avatar](https://avatars.githubusercontent.com/u/66036033?s=48)](https://github.com/QPCrummer)                                                          | qpcrummer       | Developer                 |
| [![TaterCertifed's Avatar](https://avatars.githubusercontent.com/u/98563278?s=48&u=8a1ddaf201e7c943713e4aee471ad1aa0fbe682f&v=4)](https://github.com/Tater-Certified) | Tater Certified | Contributing Organization |
