package dev.wnuke.hlktmc.cli.commands

import dev.wnuke.hlktmc.cli.CLIMessage
import dev.wnuke.hlktmc.discord.Discord
import dev.wnuke.ktcmd.Command
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val launchDiscordBot = Command<CLIMessage>("launchdb", "Launches an instance of the Discord bot.") {
    it.cli.discordBots[getArgument("name")] = Discord(getArgument("token")).apply {
        GlobalScope.launch { start() }
    }
}.apply {
    string("name", true, "Name of the Discord bot instance", "n")
    string("token", true, "Discord bot token to use", "t")
}