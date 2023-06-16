# mInetiface
**mInetiface** is a **Minecraft Fabric** mod that allows to connect to an **Intiface Central** server and thus control a connected sextoy

## Score system
There is a hidden score system that allows to compute toy intensity. There are 4 independent scoring systems, one for each of the following categories : Attack, Mining, Experience, Masochist.


The intensity is then calculated by taking the highest score from the 4 categories.

## Ingame config
You have the possibility to configure the Intiface server address, the score system and the HUD with the ingame mod config
![mInetiface ingame config](docs/config.gif "mInetiface ingame config")

## What do you need to use this mod ?
1. **Minecraft (Java Edition)**
2. **Intiface Central** [https://intiface.com/central/](https://intiface.com/central/)
3. One or multiple supported device(s) : [IoST Index – Vibrators with Buttplug.io Support](https://iostindex.com/?filter0ButtplugSupport=4&filter1Features=OutputsVibrators)


## How to install the mod
Supported versions
   <details>
     <summary>Minecraft 1.19.2</summary>
      <a href="https://mediafilez.forgecdn.net/files/3936/24/fabric-api-0.60.0%2B1.19.2.jar">Fabric API JAR</a><br>
      <a href="https://cdn.modrinth.com/data/mOgUt4GM/versions/V4hnfgRO/modmenu-4.1.2.jar">Mod Menu JAR</a><br>
      <a href="https://github.com/Fyustorm/mInetiface/releases/download/v1.19.2-1.1.0/minetiface-1.1.0-1.19.2.jar">mInetiface JAR</a>
   </details>
   <details>
     <summary>Minecraft 1.19.4</summary>
      <a href="https://mediafilez.forgecdn.net/files/4474/468/fabric-api-0.77.0%2B1.19.4.jar">Fabric API JAR</a><br>
      <a href="https://cdn.modrinth.com/data/mOgUt4GM/versions/CtMNOUcV/modmenu-6.2.3.jar">Mod Menu JAR</a><br>
      <a href="https://github.com/Fyustorm/mInetiface/releases/download/v1.19.4-1.0.0/minetiface-1.1.0-1.19.4.jar">mInetiface JAR</a>
   </details>
   <details>
     <summary>Minecraft 1.20.1</summary>
      <a href="https://mediafilez.forgecdn.net/files/4584/441/fabric-api-0.83.1%2B1.20.1.jar">Fabric API JAR</a><br>
      <a href="https://cdn.modrinth.com/data/mOgUt4GM/versions/RTFDnTKf/modmenu-7.0.1.jar">Mod Menu JAR</a><br>
      <a href="https://github.com/Fyustorm/mInetiface/releases/download/v1.20.1-1.1.0/minetiface-1.1.0-1.20.1.jar">mInetiface JAR</a>
   </details>
   
If you need another version support you can open an [issue](https://github.com/Fyustorm/mInetiface/issues/new)
1. Download and install [Minecraft Launcher](https://www.minecraft.net/en-us/download)
2. Download Fabric [https://fabricmc.net/use/installer/](https://fabricmc.net/use/installer/)
   1. Run the installer
   2. On the client tab select the Minecraft Version and click install
3. Run the Minecraft Launcher. Select the new profile named **fabric-loader-xxx**
   1. Select the **Installations** tab and click the folder icon next to the profile **fabric-loader-xx**
   2. Create **mods** folder if it doesn't exists. Example : **C:\\Users\\<username>\\AppData\\Roaming\\.minecraft\\mods**
4. Next and final step is to download all three **JAR** files for your Minecraft version and put them into the **mods** folder

   
## How to use
1. Make sure you have installed the mod and Intiface Central
2. Run the Intiface Central and start the server
3. Run the game
4. Use the command /minetiface-connect in the Minecraft chat
5. Have fun !

## Credits
This project is based on [minegasm](https://minegasm.therainbowville.com) and [Minegasm-fabric](https://github.com/vinceh121/Minegasm-fabric)
