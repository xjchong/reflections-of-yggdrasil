import org.hexworks.zircon.api.color.TileColor

object GameColor {

    val WHITE = TileColor.fromString("#FFFFFF")
    val BLACK = TileColor.fromString("#000000")
    val WALL_FOREGROUND = TileColor.fromString("#75715E")
    val WALL_BACKGROUND = TileColor.fromString("#3E3D32")
    val FLOOR_FOREGROUND = TileColor.fromString("#75715E")
    val FLOOR_BACKGROUND = TileColor.fromString("#1E2320")
    val DOOR_FOREGROUND = TileColor.fromString("#3E3D32")
    val DOOR_BACKGROUND = TileColor.fromString("#2b1e0d")
    val FOG_OF_WAR = TileColor.fromString("#000000")


    /**
     * ACTORS
     */

    val BAT = TileColor.fromString("#2348b2")
    val FUNGUS = TileColor.fromString("#5e7561")
    val PLAYER = TileColor.fromString("#FFCD22")


    /**
     * ITEMS
     */

    val EN = TileColor.fromString("#a2b8a2")
}
