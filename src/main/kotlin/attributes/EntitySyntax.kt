package attributes

import org.hexworks.amethyst.api.Attribute

data class EntitySyntax(private val syntax: HashMap<String, String> = hashMapOf()) : Attribute {

    fun putFor(owner: Any, value: String, subKey: String? = null) {
        syntax[getUniqueKeyFor(owner, subKey)] = value
    }

    fun getFor(owner: Any, subKey: String? = null): String? {
        return syntax[getUniqueKeyFor(owner, subKey)]
    }

    private fun getUniqueKeyFor(owner: Any, subKey: String?): String {
        return "${owner::class.simpleName}${if (subKey != null) ".$subKey" else ""}"
    }
}