import tools.json.Discord
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val configPath: String =
        if (args.isEmpty())
            "config"
        else
            args[0]

    val discordConfigFile = File("$configPath/discord.json")
    if (!discordConfigFile.isFile) {
        System.err.println("Could not find `$configPath/discord.json'.")
        exitProcess(2)
    }

    val discordConfig: Discord
    try {
        discordConfig = Discord.fromFile(discordConfigFile)
    }
    catch (e: Exception) {
        System.err.println(e.toString())
        System.err.println("Could not parse `$discordConfigFile' file.")
        exitProcess(2)
    }

    val api = igniteServer(configPath, discordConfig.token)
}
