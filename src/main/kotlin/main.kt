
import constants.GameConfig
import org.hexworks.zircon.api.SwingApplications
import view.StartView

fun main(args: Array<String>) {
    val application = SwingApplications.startApplication(GameConfig.buildAppConfig())
    val startView = StartView(application.tileGrid)

    startView.dock()
}