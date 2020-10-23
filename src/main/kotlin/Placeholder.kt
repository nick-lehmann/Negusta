import com.github.javafaker.Faker
import java.io.File
import kotlin.reflect.full.declaredMemberFunctions

data class Placeholder(val raw: String) {
    var scope = ""
    var name = ""
    init {
        val (scope, name) = raw.split(".")
        this.scope = scope
        this.name = name
    }
}

fun getCustomPlaceholders(customPath: String = "~/.negusta"): List<String> {
    val file = File(customPath.replaceFirst("~", System.getProperty("user.home")))
    if (!file.exists())
        return listOf("")
    return file.walk()
            .filter { it.name.endsWith(".txt") }
            .map { it.nameWithoutExtension }
            .toList()
}


fun getBuiltinPlaceholders(): MutableMap<String, List<String>> {
    val placeholderMap = mutableMapOf<String, List<String>>()
    for (scope in Faker::class.declaredMemberFunctions.map { method -> method.name }) {
        if (scope == "instance" || scope.contains("Service")) continue

        val fakerMethod = try {
            Faker::class.java.getMethod(scope)
        } catch (e: Exception) { continue }

        val newMethods = fakerMethod.returnType.methods
                .filter { method -> method.returnType.name == "java.lang.String" && method.name != "toString" }
                .map { method -> method.name }
                .toSet().toList()
        if (newMethods.isNotEmpty()) placeholderMap[scope] = newMethods
    }
    return placeholderMap
}


fun getAllPlaceholders(onlyCustom: Boolean = false, onlyBuiltin: Boolean = false): Map<String, List<String>> {
    val includeCustom = !onlyBuiltin; val includeBuiltin = !onlyCustom

    val placeholderMap = if (includeBuiltin) getBuiltinPlaceholders() else mutableMapOf()
    if (includeCustom) placeholderMap["custom"] = getCustomPlaceholders()

    return placeholderMap
}


fun getAllPlaceholdersFlat(): List<String> {
    return getAllPlaceholders().entries.map { (scope, names) -> names.map { name -> "$scope.$name"} }.flatten()
}
