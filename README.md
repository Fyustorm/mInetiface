> ⚠️ **Warning**
> This mod is no longer maintained. If you want to take over the project, feel free to contact me.

# mInetiface
**mInetiface** is a Minecraft mod for **Fabric** and **Forge** that allows to connect to an **Intiface Central** server and thus control a connected sextoy

## Score system
There are 4 independent scoring systems, one for each of the following categories : **Attack**, **Mining**, **Experience**, **Masochist**.
You accumulate points for a category each time you do an action from that category. You keep your points for the cagetories for a configurable time before starting to lose your points over time.

The intensity is then calculated by taking the highest score from the 4 categories.

## Config
The old in-game configuration for the mInetiface mod has been replaced by the [mInetiface-config editor](https://github.com/Fyustorm/mInetiface-config). Please use the mInetiface-config application to manage all your mod settings.

![mInetiface config editor screenshot](https://github.com/Fyustorm/mInetiface-config/blob/master/doc/main.png)

If you don't want to use config editor you can edit the configuration manually.

Config file location :
_<minecraft_folder>_**/config/minetiface.config**

Changes to the file are automatically applied. You don't need to reload your game.
<details>
<summary>File configuration description</summary>

<b>Intiface</b>
<ul>
<li><b>serverUrl</b> : Buttplug server URL</li>
<li><b>fullMaxTime</b> : For linear devices, the maximum time for a full stroke in ms (min speed)</li>
<li><b>fullMinTime</b> : For linear devices, the minimum time for a full stroke in ms (max speed)</li>
</ul>

<b>General actions</b>
<ul>
<li><b>minimumFeedback</b> : Minimum feedback intensity</li>
<li><b>maximumFeedback</b> : Maximum feedback intensity</li>
<li><b>feedbackScoreLostPerTick</b> : Number of feedback points lost per tick (20 ticks/s)</li>
<li><b>scoreLostPerTick</b> : Number of score points lost per tick for a category when the duration to keep score is 0 (see <b>maximumSecondsKeepScore</b> and <b>***durationMultiplier</b>) </li>
</ul>

<b>Attack actions</b>
<ul>
<li><b>attackEnabled</b> : Enable attack actions ?</li>
<li><b>attackMultiplier</b> : Points multiplier for attack actions</li>
<li><b>attackInstantPointsMultiplier</b> : Feedback multiplier for attack feedback actions</li>
<li><b>attackDurationMultiplier</b> : Time to keep attack points multiplier</li>
</ul>

<b>Mining actions</b>
<ul>
<li><b>miningEnabled</b> : Enable mining actions ?</li>
<li><b>minePointsMultiplier</b> : Points multiplier for mining actions</li>
<li><b>mineInstantPointsMultiplier</b> : Feedback multiplier for mining feedback actions</li>
<li><b>mineDurationMultiplier</b> : Time to keep mining points multiplier</li>
<li><b>blocksScore</b> : List of blocks and their score for mining</li>
<li><b>defaultBlockScore</b> : Default score for mining blocks that are not in the <b>blocksScore</b> list</li>
</ul>

<b>Experience actions</b>
<ul>
<li><b>xpEnabled</b> : Enable experience actions ?</li>
<li><b>xpMultiplier</b> : Points multiplier for experience actions</li>
<li><b>xpInstantPointsMultiplier</b> : Feedback multiplier for experience feedback actions</li>
<li><b>xpDurationMultiplier</b> : Time to keep experience points multiplier</li>
</ul>

<b>Masochist actions</b>
<ul>
<li><b>masochistEnabled</b> : Enable masochist actions ?</li>
<li><b>masochistMultiplier</b> : Points multiplier for masochist actions</li>
<li><b>masochistInstantPointsMultiplier</b> : Feedback multiplier for masochist feedback actions</li>
<li><b>masochistDurationMultiplier</b> : Time to keep masochist points multiplier</li>
</ul>

</details>

## What do you need to use this mod ?
1. **Minecraft (Java Edition)**
2. **Intiface Central** [https://intiface.com/central/](https://intiface.com/central/)
3. One or multiple supported device(s) : [IoST Index – Vibrators with Buttplug.io Support](https://iostindex.com/?filter0ButtplugSupport=4&filter1Features=OutputsVibrators)

## How to install the mod
All versions can be downloaded from the [mInetiface website](https://minetiface.fyustorm.ovh/releases) or directly from the GitHub releases page.
If you need another version support you can open an [issue](https://github.com/Fyustorm/mInetiface/issues/new)

If you don't know what are **Forge** and **Fabric** and you only want to use the **mInetiface** mod you can follow the Fabric instructions below.

### Fabric
1. Download and install [Minecraft Launcher](https://www.minecraft.net/en-us/download) or any launcher you want
2. Download Fabric [https://fabricmc.net/use/installer/](https://fabricmc.net/use/installer/)
   1. Run the installer
      1. On the client tab select the Minecraft Version and click install
      2. Run the Minecraft Launcher. Select the new profile named **fabric-loader-xxx**
         1. Select the **Installations** tab and click the folder icon next to the profile **fabric-loader-xx**
         2. Create **mods** folder if it doesn't exists. Example : **C:\\Users\\<username>\\AppData\\Roaming\\.minecraft\\mods**
3. [Download](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?page=1&pageSize=20&gameVersionTypeId=4&showAlphaFiles=hide) the fabric-api **JAR** for your Minecraft version and put it into the **mods** folder
5. [Download](https://minetiface.fyustorm.ovh/releases) the mInetiface **JAR** for your Minecraft version and put it into the **mods** folder

### Forge
1. Download the **JAR** file for your Minecraft version and put it into your **mods** folder

## How to use
1. Make sure you have installed the mod and Intiface Central
2. Run the Intiface Central and start the server
3. Run the game
4. Use the command /minetiface-connect in the Minecraft chat
5. Have fun !

## Credits
This project is based on [minegasm](https://minegasm.therainbowville.com) and [Minegasm-fabric](https://github.com/vinceh121/Minegasm-fabric)

Special thanks to [BlackSphereFollower](https://github.com/blackspherefollower) for technical support :)

## Developers
The main branch is only use for the README. The source code is not up to date on the main branch.
