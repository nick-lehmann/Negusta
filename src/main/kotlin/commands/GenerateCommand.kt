package commands

import CUSTOM_DIRECTORY
import Placeholder
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.javafaker.Faker
import didYouMean
import getAllPlaceholdersFlat
import java.io.File

class GenerateCommand : CliktCommand(help="Generate fake data") {
    val name = "generate"
    val template: String by argument(help="The format you desire")
    val lines: Int by option(help="Number of lines you want to get").int().default(1)
    val locale: String by option(help="Locale to use for faker").default("en")
    val customDirectory: String by option(help="Path to a directory full of value files").default(CUSTOM_DIRECTORY)

    val faker = Faker()

    private fun getBuiltinFakedValue(placeholder: Placeholder): String {
        val scopeMethod = Faker::class.java.getMethod(placeholder.scope)
        val fakerObject = scopeMethod.invoke(faker) as Any
        val fakerMethod = fakerObject::class.java.getMethod(placeholder.name)
        return fakerMethod.invoke(fakerObject) as String
    }

    private fun getCustomFakedValue(placeholder: Placeholder): String {
        val file = File(this.customDirectory.replaceFirst("~", System.getProperty("user.home")))

        val valueFiles = file.walk().filter { it.name.endsWith(".txt") }.toList()
        val targetFile = valueFiles.find { it.nameWithoutExtension == placeholder.name } ?: return ""
        return targetFile.readLines().random()
    }

    private fun getFakedValue(placeholder: Placeholder): String =
            if (placeholder.scope == "custom") getCustomFakedValue(placeholder) else getBuiltinFakedValue(placeholder)

    private fun generateLine(template: String): String {
        // TODO: Replace with regex that parses scope and name directly
        val regex = "\\{[\\w.]+\\}".toRegex()
        var line = template
        var offset = 0

        val availablePlaceholder = getAllPlaceholdersFlat()

        for(match in regex.findAll(template)) {
            val wordGroup = match.groups[0]!!
            val placeholderRaw = wordGroup.value.trimStart('{').trimEnd('}')

            if (!availablePlaceholder.contains(placeholderRaw)) {
                throw IllegalArgumentException("$placeholderRaw is not a valid placeholder. Did you mean ${didYouMean(availablePlaceholder, placeholderRaw)[0]}")
            }

            val placeholder = Placeholder(placeholderRaw)

            val newValue = this.getFakedValue(placeholder)
            line = line.replaceRange(
                    startIndex = wordGroup.range.first - offset,
                    endIndex = wordGroup.range.last + 1 - offset,
                    replacement = newValue
            )
            offset += wordGroup.value.length - newValue.length
        }

        return line
    }


    override fun run () {
        repeat(lines) {
            println(generateLine(template))
        }
    }
}
