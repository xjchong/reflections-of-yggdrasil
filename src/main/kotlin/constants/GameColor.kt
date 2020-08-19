import org.hexworks.zircon.api.builder.component.ColorThemeBuilder
import org.hexworks.zircon.api.color.TileColor
import kotlin.random.Random

object GameColor {

    /**
     * PALETTE
     */

    val WHITE           = hexColor("e1ddcf")
    val BLACK           = hexColor("202020")

    val LIGHT_GREY      = hexColor("afab9e")
    val GREY            = hexColor("7d7a70")
    val DARK_GREY       = hexColor("4a4843")

    val LIGHT_BROWN     = hexColor("a67233")
    val BROWN           = hexColor("734f24")
    val DARK_BROWN      = hexColor("402c14")

    val LIGHT_PINK      = hexColor("ea5ba0")
    val PINK            = hexColor("d33682")
    val DARK_PINK       = hexColor("872354")

    val LIGHT_RED       = hexColor("ef5451")
    val RED         	 = hexColor("dc322f")
    val DARK_RED        = hexColor("8d201e")

    val LIGHT_ORANGE    = hexColor("f79f38")
    val ORANGE      	 = hexColor("d77504")
    val DARK_ORANGE     = hexColor("8a4b03")

    val LIGHT_YELLOW    = hexColor("b59a48")
    val YELLOW      	 = hexColor("b58900")
    val DARK_YELLOW     = hexColor("745800")

    val LIGHT_GREEN     = hexColor("abbd33")
    val GREEN       	 = hexColor("859900")
    val DARK_GREEN      = hexColor("556200")

    val LIGHT_CYAN      = hexColor("56c2bb")
    val CYAN        	 = hexColor("2aa198")
    val DARK_CYAN       = hexColor("1b6761")

    val LIGHT_BLUE      = hexColor("4fabec")
    val BLUE        	 = hexColor("268bd2")
    val DARK_BLUE       = hexColor("185987")

    val LIGHT_VIOLET    = hexColor("9094e0")
    val VIOLET      	 = hexColor("6c71c4")
    val DARK_VIOLET     = hexColor("45487d")


    /**
     * EFFECTS
     */

    val DAMAGE_FLASH    = RED
    val DESTROY_FLASH   = DARK_VIOLET
    val GUARD_FLASH     = BLUE
    val POISON_FLASH    = VIOLET


    /**
     * APPLICATION
     */

    val ACCENT = GameColor.YELLOW
    val FOREGROUND = GameColor.LIGHT_GREY
    val SECONDARY_FOREGROUND = GameColor.GREY
    val BACKGROUND = GameColor.BLACK
    val SECONDARY_BACKGROUND = BLACK.lightenByPercent(0.3).desaturate(0.3)

    val FROSTED_BACKGROUND = BACKGROUND.withAlpha(215)

    val MAIN_THEME = ColorThemeBuilder.newBuilder()
            .withAccentColor(ACCENT)
            .withPrimaryForegroundColor(FOREGROUND)
            .withSecondaryForegroundColor(SECONDARY_FOREGROUND)
            .withPrimaryBackgroundColor(BACKGROUND)
            .withSecondaryBackgroundColor(SECONDARY_BACKGROUND)
            .build()

    val TRANSPARENT_THEME = ColorThemeBuilder.newBuilder()
            .withAccentColor(ACCENT)
            .withPrimaryForegroundColor(FOREGROUND)
            .withSecondaryForegroundColor(SECONDARY_FOREGROUND)
            .withPrimaryBackgroundColor(BACKGROUND.withAlpha(0))
            .withSecondaryBackgroundColor(SECONDARY_BACKGROUND.withAlpha(0))
            .build()


    /**
     * HELPERS
     */

    private fun hexColor(hexString: String): TileColor {
        return TileColor.fromString("#$hexString)")
    }
}


fun TileColor.withVariance(variance: Double = 0.2): TileColor {
    return this
            .lightenByPercent(Random.nextDouble(0.0, variance))
            .desaturate(Random.nextDouble(0.0, variance))
            .darkenByPercent(Random.nextDouble(0.0, variance))
}
