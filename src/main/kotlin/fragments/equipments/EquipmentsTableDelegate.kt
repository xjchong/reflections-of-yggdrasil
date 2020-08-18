package fragments.equipments

import attributes.Equipments
import entity.GameEntity
import entity.NoType
import extensions.optional
import fragments.TableFragmentDelegate
import fragments.TableRow


class EquipmentsTableDelegate(
    private val equipments: Equipments,
    private val onUnequip: (GameEntity) -> Unit,
    private val onExamine: (GameEntity) -> Unit
) : TableFragmentDelegate() {

    init {
       equipments.dataTimestamp.onChange  {
           notifyDataChanged()
       }
    }

    override fun rowCount(): Int {
        return equipments.equipped.count()
    }

    override fun itemForRow(row: Int): TableRow {
        val equipment = equipments.equipped.getOrNull(row)?.optional ?: NoType.create()
        val rowFragment = EquipmentRow(EquipmentsTable.width, equipment)

        with(rowFragment) {
            rowFragment.unequipButton.onActivated { onUnequip(equipment) }
            rowFragment.onExamine = onExamine
        }

        return rowFragment
    }
}