package builders

import entity.AnyEntity
import entity.NoType
import game.GameContext
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.builder.EntityBuilder
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.amethyst.api.newEntityOfType
import org.hexworks.zircon.api.builder.Builder


fun <T : EntityType> newGameEntityOfType(type: T, init: EntityBuilder<T, GameContext>.() -> Unit) =
        newEntityOfType<T, GameContext>(type, init)

data class AnyEntityBuilder(
        private var type: EntityType = NoType,
        private var attributes: LinkedHashSet<Attribute> = linkedSetOf(),
        private var behaviors: LinkedHashSet<BaseBehavior<GameContext>> = linkedSetOf(),
        private var facets: LinkedHashSet<BaseFacet<GameContext>> = linkedSetOf()) : Builder<AnyEntity> {

    companion object {
        fun newBuilder(type: EntityType) = AnyEntityBuilder(type)
    }

    override fun build(): AnyEntity = newGameEntityOfType(type) {
        attributes(*this@AnyEntityBuilder.attributes.toTypedArray())
        behaviors(*this@AnyEntityBuilder.behaviors.toTypedArray())
        facets(*this@AnyEntityBuilder.facets.toTypedArray())
    }

    override fun createCopy(): Builder<AnyEntity> = copy()

    fun withAddedAttributes(vararg attributes: Attribute) = also {
        this.attributes.addAll(attributes.toList())
    }

    fun withRemovedAttributes(vararg attributes: Attribute) = also {
        this.attributes.removeAll(attributes.toList())
    }

    fun withAttributes(vararg attributes: Attribute) = also {
        this.attributes.clear()
        this.attributes.addAll(attributes.toList())
    }

    fun withAddedBehaviors(vararg behaviors: BaseBehavior<GameContext>) = also {
        this.behaviors.addAll(behaviors.toList())
    }

    fun withRemovedBehaviors(vararg behaviors: BaseBehavior<GameContext>) = also {
        this.behaviors.removeAll(behaviors.toList())
    }

    fun withBehaviors(vararg behaviors: BaseBehavior<GameContext>) = also {
        this.behaviors.clear()
        this.behaviors.addAll(behaviors.toList())
    }

    fun withAddedFacets(vararg facets: BaseFacet<GameContext>) = also {
        this.facets.addAll(facets.toList())
    }

    fun withRemovedFacets(vararg facets: BaseFacet<GameContext>) = also {
        this.facets.removeAll(facets.toList())
    }

    fun withFacets(vararg facets: BaseFacet<GameContext>) = also {
        this.facets.clear()
        this.facets.addAll(facets.toList())
    }
}