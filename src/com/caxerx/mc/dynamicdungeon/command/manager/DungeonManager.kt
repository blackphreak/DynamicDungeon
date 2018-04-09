package com.caxerx.mc.dynamicdungeon.command.manager

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.experimental.async
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObject
import me.blackphreak.dynamicdungeon.dungeonobject.DungeonObjectSerDes
import me.blackphreak.dynamicdungeon.gb
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset

object DungeonManager {
    private lateinit var dungeonMap: HashMap<String, List<DungeonObject>>
    private val gson: Gson = GsonBuilder().registerTypeHierarchyAdapter(DungeonObject::class.java, DungeonObjectSerDes()).create()


    init {
        readFromFile()
    }

    fun getDungeon(dungeonName: String) = dungeonMap[dungeonName]

    fun hasDungeon(dungeonName: String) = dungeonName in dungeonMap

    fun getDungeons() = dungeonMap.keys

    fun createDungeon(dungeonName: String) = dungeonMap.put(dungeonName, mutableListOf())

    fun readFromFile() {
        async {
            dungeonMap = HashMap()
            File(gb.dataPath).listFiles().forEach { file ->
                if (file.name.endsWith(".json")) {
                    var dungeon = listOf(*gson.fromJson(FileUtils.readFileToString(file, Charset.defaultCharset()), Array<DungeonObject>::class.java))
                    dungeonMap[file.nameWithoutExtension] = dungeon
                }
            }
        }
    }

    fun saveToFile() {
        async {
            dungeonMap.forEach { name, dungeon ->
                FileUtils.writeStringToFile(File(gb.dataPath, "$name.json"), gson.toJson(dungeon), Charset.defaultCharset())
            }
        }
    }
}