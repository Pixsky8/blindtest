package tools

class Option {
    val dryRun: Boolean
    val help: Boolean
    val configPath: String

    constructor(args: Array<String>) {
        var help: Boolean = false
        var dryRun: Boolean = false
        var configPath: String = "config"

        for (arg in args) {
            if (arg == "-d" || arg == "--dry-run")
                dryRun = true
            else if (arg == "-h" || arg == "--help")
                help = true
            else
                configPath = arg
        }

        this.dryRun = dryRun
        this.help = help
        this.configPath = configPath
    }
}
