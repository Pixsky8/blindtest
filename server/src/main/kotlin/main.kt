import tools.json.Discord
import java.io.File

fun main(args: Array<String>) {
    val discordConfigFile = File("config/discord.json")
    if (!discordConfigFile.isFile) {
        System.err.println("Could not find `config/discord.json'.")
        System.exit(2)
    }

    val discordConfig: Discord
    try {
        discordConfig = Discord.fromFile(discordConfigFile)
    }
    catch (e: Exception) {
        System.err.println(e.toString())
        System.err.println("Could not parse `config/discord.json' file.")
        System.exit(2)
        return
    }

    val api = igniteServer(discordConfig.token)
}
