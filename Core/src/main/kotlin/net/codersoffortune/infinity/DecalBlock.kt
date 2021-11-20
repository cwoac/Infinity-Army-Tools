package net.codersoffortune.infinity

/**
 * A block of text representing the whole decal for TTS
 *
 * TODO:: merge with DecalBlockModel
 */
data class DecalBlock(val decal: String) {
    private val decalRegex: Regex = """"ImageURL":"([^"]*)"""".toRegex()

    fun getImageUrls() : Collection<String> {
        val matches = decalRegex.findAll(decal)
        return matches.map {it.groupValues[1]}.toList()
    }

    fun getDecals() : List<Decal> {
        val matches = decalRegex.findAll(decal)
        return matches.map {Decal(it.groupValues[1])}.toList()
    }
}