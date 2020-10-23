/**
 * Returns the placeholder that is closest to the actual input.
 */
fun didYouMean(allPlaceholder: List<String>, input: String, limit: Int = 1): List<String> {
    return allPlaceholder.sortedBy { placeholder -> levenshtein(placeholder, input) }.take(limit)
}

/**
 * Returns the cost of changing text1 into text2.
 *
 * It is used to find the most similar valid placeholder to a invalid input.
 */
fun levenshtein(text1 : CharSequence, text2 : CharSequence) : Int {
    val lhsLength = text1.length
    val rhsLength = text2.length

    var cost = Array(lhsLength) { it }
    var newCost = Array(lhsLength) { 0 }

    for (i in 1 until rhsLength) {
        newCost[0] = i

        for (j in 1 until lhsLength) {
            val match = if(text1[j - 1] == text2[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = Math.min(Math.min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[lhsLength - 1]
}
