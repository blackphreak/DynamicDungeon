# DynamicDungeon
a RPG Dungeon plugin designed for you, it is lite and easy to use.

Hello, this is my first minecraft open source project.
This project is design for RPG Server.
DynamicDungeon able to create a Dungeon Session for a party(team) / one player to play using the same world.

# How it works?
When a player(or partyLeader) right click the wall_sign(which is dungeon entrance), this plugin will create a DungeonSession.
Each DungeonSession will load WorldEdit Schematic file to the world that just for dungeon, which is a empty world named as "dungeonWorld".
The DungeonSession will spawn mobs that set on the map(schematic world) with Sign. All the mobs reqiure a MythicMob Spawner to spawn, plugin won't spawn a mob by itself.

When all players leave/offline, the DungeonSession will be killed by itself and remove the blocks that copied from schematic to save the server resource.

# Features
* load dungeon by import WorldEdit Schematic file
* create dungeon independently for each party/player
* set Check Point, Spawn Point and End Point
* no more queueing for dungeon
* using AsyncWorldEdit to create DungeonSession

# Requirements
Plugin only tested on this environment:
* PaperSpigot #1139
* WorldEdit #3690
* MythicMobs 4.1.1 or higher
* AsyncWorldEdit 3.4.10
