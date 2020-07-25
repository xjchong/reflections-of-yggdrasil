package attributes

import entity.AnyEntity
import org.hexworks.zircon.api.component.Component


class EnmityList : DisplayableAttribute {

    val entities: MutableList<AnyEntity> = mutableListOf()

    override fun toComponent(width: Int): Component {
        TODO("Not yet implemented")
    }
}