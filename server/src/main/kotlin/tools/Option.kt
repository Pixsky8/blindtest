package tools

import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.slf4j.Logger.ROOT_LOGGER_NAME

class Option {
    val debug: Boolean
    val dryRun: Boolean
    val help: Boolean
    val configPath: String

    constructor(args: Array<String>) {
        var help: Boolean = false
        var debug: Boolean = false
        var dryRun: Boolean = false
        var configPath: String = "config"

        for (arg in args) {
            if (arg == "-d" || arg == "--dry-run")
                dryRun = true
            else if (arg == "-h" || arg == "--help")
                help = true
            else if (arg == "--debug")
                debug = true
            else
                configPath = arg
        }

        this.debug = debug
        this.dryRun = dryRun
        this.help = help
        this.configPath = configPath

        if (debug)
            (LoggerFactory.getLogger(ROOT_LOGGER_NAME) as Logger).level = Level.DEBUG
    }
}
