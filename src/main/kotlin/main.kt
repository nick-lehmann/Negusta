import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import commands.GenerateCommand
import commands.ListCommand

val CUSTOM_DIRECTORY = "~/.negusta"

class Negusta : CliktCommand() {
    override fun run() {}
}

fun main(args: Array<String>) {
    Negusta()
        .subcommands(GenerateCommand())
        .subcommands(ListCommand())
        .main(args)
}