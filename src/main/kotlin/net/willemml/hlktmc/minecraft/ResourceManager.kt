package net.willemml.hlktmc.minecraft

import com.github.steveice10.mc.protocol.MinecraftConstants
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.willemml.hlktmc.minecraft.objects.Block
import net.willemml.hlktmc.minecraft.objects.BoundingBox
import net.willemml.hlktmc.minecraft.objects.Item
import net.willemml.hlktmc.minecraft.objects.Material
import java.io.File

val AIR_BLOCK = Block(
    id = 0,
    displayName = "Air",
    name = "air",
    hardness = 0.0f,
    stackSize = 0,
    diggable = true,
    BoundingBox.empty,
    material = null,
    drops = emptyArray(),
    emitLight = 0,
    filterLight = 0,
    transparent = true
)

object ResourceManager {
    private val classLoader = javaClass.classLoader
    private val json = Json { ignoreUnknownKeys = true }

    var dataPaths = HashMap<String, HashMap<String, HashMap<String, String>>>()

    var blocks = HashMap<Int, Block>()
    var items = HashMap<Int, Item>()
    var materials = HashMap<String, Material>()

    fun readJsonStringFromResource(path: String)
	= classLoader.getResourceAsStream(path) ?.let { it.bufferedReader().use { it.readText() } }
    
    fun loadPaths() {
        dataPaths = readJsonStringFromResource("minecraft-data/data/dataPaths.json") ?.let { json.decodeFromString(it) } ?: return
    }

    fun loadBlocks() {
        val blocksPath = dataPaths["pc"]?.get(MinecraftConstants.GAME_VERSION)?.get("blocks") ?: return
        val blocksArray = readJsonStringFromResource("minecraft-data/data/$blocksPath/blocks.json") ?.let { json.decodeFromString<Array<Block>>(it) } ?: return
        for (block in blocksArray) blocks[block.id] = block
        if (!blocks.containsKey(0)) blocks[0] = AIR_BLOCK
    }

    fun loadItems() {
        val itemsPath = dataPaths["pc"]?.get(MinecraftConstants.GAME_VERSION)?.get("items") ?: return
        val itemsArray = readJsonStringFromResource("minecraft-data/data/$itemsPath/items.json") ?.let { json.decodeFromString<Array<Item>>(it) } ?: return
        for (item in itemsArray) items[item.id] = item
    }

    fun loadMaterials() {
        val materialsPath = dataPaths["pc"]?.get(MinecraftConstants.GAME_VERSION)?.get("materials") ?: return
        materials = readJsonStringFromResource("minecraft-data/data/$materialsPath/materials.json") ?.let { json.decodeFromString(it) } ?: return
    }
}
