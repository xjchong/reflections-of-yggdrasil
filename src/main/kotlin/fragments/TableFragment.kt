package fragments

import org.hexworks.cobalt.databinding.api.extension.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.AttachedComponent
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.internal.component.modal.EmptyModalResult

abstract class TableRow(val width: Int, val height: Int) : Fragment

abstract class TableFragmentDelegate() {

    private val lastDataChangeProp: Property<Long> = createPropertyFrom(System.currentTimeMillis())

    abstract fun rowCount(): Int
    abstract fun itemForRow(row: Int): TableRow

    fun notifyDataChanged() {
        lastDataChangeProp.updateValue(System.currentTimeMillis())
    }

    fun onDataChanged(table: TableFragment, onChange: () -> Unit) {
        lastDataChangeProp.onChange { onChange() }
    }
}

abstract class TableFragment(val width: Int, val height: Int) : Fragment {

    var parentModal: Modal<EmptyModalResult>? = null
    var delegate: TableFragmentDelegate? = null
        set(value) {
            field = value
            value?.onDataChanged(this) { update() }
            update()
        }

    private val attachedRows: MutableList<AttachedComponent> = mutableListOf()
    private val vBox: VBox by lazy { root as VBox }

    override val root: Component = Components.vbox()
        .withSize(width, height)
        .build().apply { update() }

    private fun update() {
        val immutableDelegate = delegate ?: return

        while (attachedRows.isNotEmpty()) {
            attachedRows.removeAt(0).detach()
        }

        repeat(immutableDelegate.rowCount()) { index ->
            val rowFragment = immutableDelegate.itemForRow(index)

            attachedRows.add(vBox.addFragment(rowFragment))
        }

        requestFocus()
    }

    private fun requestFocus() {
        parentModal?.requestFocus()
    }
}