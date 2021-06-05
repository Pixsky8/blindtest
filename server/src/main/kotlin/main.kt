import repository.QuestionsRepository
import tools.Option
import tools.json.Discord
import java.io.File
import kotlin.system.exitProcess

fun getDiscordConfig(option: Option): Discord {
    val discordConfigFile = File("${option.configPath}/discord.json")
    if (!discordConfigFile.isFile) {
        System.err.println("Could not find `${option.configPath}/discord.json'.")
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

    return discordConfig
}

fun main(args: Array<String>) {
    val option = Option(args)

    if (option.help) {
        println("Usage:")
        println("\t-h, --help: display this message")
        println("\t-d, --dry-run: test the config files")
        println("\t--debug: display debug logs")
        return
    }

    val discordConfig = getDiscordConfig(option)

    if (option.dryRun) {
        QuestionsRepository("${option.configPath}/questions.json")
        return
    }

    val api = igniteServer(option, discordConfig.token)
}
