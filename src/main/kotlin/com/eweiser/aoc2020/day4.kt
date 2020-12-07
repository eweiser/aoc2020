package main.kotlin.com.eweiser.aoc2020

fun main(args:Array<String>) {
    val input = {}.javaClass.enclosingClass.getResource("/day4.txt").readText().lines()
    println(validateInput(input, false))
    println(validateInput(input, true))
}

fun validateInput(input: List<String>, validateValues: Boolean): Int {
    var numValidPassports = 0
    val validator = PassportValidator()
    for (line in input) {
        if (line == "") {
            if (validator.validate(validateValues)) {
                numValidPassports += 1
            } else {
                println(validator.getInvalidFields())
            }
            validator.reset()
        } else {
            for (token in line.split(Regex("\\s+"))) {
                val (field, value) = token.split(":", limit = 2)
                validator.addField(field, value)
            }
        }
    }
    if (validator.validate(validateValues)) {
        numValidPassports += 1
    } else {
        println(validator.getInvalidFields())
    }

    return numValidPassports
}

class PassportValidator {

    private val FIELD_VALIDATORS = mapOf(
        "byr" to {value: String -> value.length == 4 && IntRange(1920, 2002).contains(value.toIntOrNull())},
        "iyr" to {value: String -> value.length == 4 && IntRange(2010, 2020).contains(value.toIntOrNull())},
        "eyr" to {value: String -> value.length == 4 && IntRange(2020, 2030).contains(value.toIntOrNull())},
        "hgt" to this::validateHeight,
        "hcl" to {value: String -> Regex("#[0-9a-f]{6}").matches(value)},
        "ecl" to {value: String -> setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(value) },
        "pid" to {value: String -> Regex("[0-9]{9}").matches(value)}
    )
    var data = mutableMapOf<String,String>()

    fun reset() {
        data = mutableMapOf()
    }

    fun addField(field: String, value: String) {
        data[field] = value
    }

    fun validate(validateValues: Boolean): Boolean {
        val hasAllRequiredFields = data.keys.containsAll(FIELD_VALIDATORS.keys)
        val allFieldsValid = data.entries.map { (key, value) -> validateField(key, value) }.all { it }

        return if (validateValues) hasAllRequiredFields && allFieldsValid else hasAllRequiredFields
    }

    fun getInvalidFields(): Set<Pair<String, String>> {
        return data.entries.filter { (key, value) -> !validateField(key, value) }.map { it.toPair() }.toSet()
    }

    private fun validateField(field: String, value: String): Boolean {
        val validateFn = FIELD_VALIDATORS[field]
        return if (validateFn != null) validateFn(value) else true
    }

    private fun validateHeight(height: String): Boolean {
        val heightRegex = Regex("(\\d+)(cm|in)")
        val matchResult = heightRegex.find(height)
        return if (matchResult != null) {
            val heightNum = matchResult.groupValues[1].toIntOrNull()
            val unit = matchResult.groupValues[2]
            val heightRange = if (unit == "cm") IntRange(150, 193) else IntRange(59, 76)
            heightRange.contains(heightNum)
        } else {
            false
        }

    }
}