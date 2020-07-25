import org.hexworks.zircon.api.builder.component.ColorThemeBuilder
import org.hexworks.zircon.api.color.TileColor

object GameColor {

    /**
     * PALETTE
     */

    val WHITE           = hexColor("e1ddcf")
    val BLACK           = hexColor("202020")

    val GREY            = hexColor("afab9e")
    val LIGHT_GREY      = GREY.light
    val DARK_GREY       = GREY.dark

    val BROWN       	= hexColor("402c14")
    val LIGHT_BROWN     = BROWN.light
    val DARK_BROWN      = BROWN.dark

    val PINK        	= hexColor("d33682")
    val LIGHT_PINK      = PINK.light
    val DARK_PINK       = PINK.dark

    val RED         	= hexColor("dc322f")
    val LIGHT_RED       = RED.light
    val DARK_RED        = RED.dark

    val ORANGE      	= hexColor("d77504")
    val LIGHT_ORANGE    = ORANGE.light
    val DARK_ORANGE     = ORANGE.dark

    val YELLOW      	= hexColor("b58900")
    val LIGHT_YELLOW    = YELLOW.light
    val DARK_YELLOW     = YELLOW.dark

    val GREEN       	= hexColor("859900")
    val LIGHT_GREEN     = GREEN.light
    val DARK_GREEN      = GREEN.dark

    val CYAN        	= hexColor("2aa198")
    val LIGHT_CYAN      = CYAN.light
    val DARK_CYAN       = CYAN.dark

    val BLUE        	= hexColor("268bd2")
    val LIGHT_BLUE      = BLUE.light
    val DARK_BLUE       = BLUE.dark

    val VIOLET      	= hexColor("6c71c4")
    val LIGHT_VIOLET    = VIOLET.light
    val DARK_VIOLET     = VIOLET.dark


    /**
     * ENVIRONMENT
     */

    val FOG_OF_WAR      = BLACK

    val WALL            = GREY
    val FLOOR           = GREY
    val DOOR            = GREY
    val DOOR_BACKGROUND = BROWN

    val DAMAGE_FLASH    = RED
    val DESTROY_FLASH   = DARK_VIOLET


    /**
     * ACTORS
     */

    val BAT     = DARK_BLUE
    val FUNGUS  = GREEN
    val PLAYER  = YELLOW


    /**
     * ITEMS
     */

    val BAT_MEAT    = DARK_PINK
    val EN          = LIGHT_CYAN


    /**
     * APPLICATION
     */

    val ACCENT = GameColor.YELLOW
    val FOREGROUND = GameColor.WHITE
    val SECONDARY_FOREGROUND = GameColor.GREY
    val BACKGROUND = GameColor.BLACK
    val SECONDARY_BACKGROUND = BLACK.light

    val THEME = ColorThemeBuilder.newBuilder()
            .withAccentColor(ACCENT)
            .withPrimaryForegroundColor(FOREGROUND)
            .withSecondaryForegroundColor(SECONDARY_FOREGROUND)
            .withPrimaryBackgroundColor(BACKGROUND)
            .withSecondaryBackgroundColor(SECONDARY_BACKGROUND)
            .build()


    /**
     * HELPERS
     */

    private fun hexColor(hexString: String): TileColor {
        return TileColor.fromString("#$hexString)")
    }

    private val TileColor.light: TileColor
        get() = this.lightenByPercent(0.3).desaturate(0.3)

    private val TileColor.dark: TileColor
        get() = this.darkenByPercent(0.2).shade(0.2)
}
