package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.MutuallyExclusiveGroupException
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import getAllPlaceholders

class ListCommand : CliktCommand(help="List all possible placeholders") {
    val name = "list"
    val onlyBuiltin: Boolean by option(help="Only display builtin placeholders").flag()
    val onlyCustom: Boolean by option(help="Only display custom placeholders").flag()

    override fun run() {
        if (onlyBuiltin && onlyCustom)
            throw MutuallyExclusiveGroupException(listOf("--onlyBuiltin", "--onlyCustom"))

        for ( (scope, names) in getAllPlaceholders(onlyBuiltin = onlyBuiltin, onlyCustom = onlyCustom).entries ) {
            println(scope)
            for (name in names) println(" - $name")
        }
    }
}
