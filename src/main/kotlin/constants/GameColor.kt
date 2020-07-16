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

    val PLAYER_COLOR = TileColor.fromString("#FFCD22")
    val FUNGUS_COLOR = TileColor.fromString("#5e7561")
}
