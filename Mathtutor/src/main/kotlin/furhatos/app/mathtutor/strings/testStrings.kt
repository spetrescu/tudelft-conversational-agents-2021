package furhatos.app.mathtutor.strings

import furhatos.flow.kotlin.Furhat
import furhatos.util.Language

interface TestStrings : DataString {
    val welcome: String
    val solveExercises: String
    val askNoOfQuestions: String
    fun confirmNoOfQuestion(noOfQuestion: Int): String
    val askDifficulty: String
    val difficultyLevels: List<String>
    fun confirmDifficulty(chosenDifficulty: String): String
    val askToStartTest: String
    val testStarts: String
    val multiplication: String
    val division: String
    val addition: String
    val subtraction: String
    val percentages: String
    val fractions: String
    val whatIs: String
    fun percentage(operandOne: Int, operandTwo: Int): String
}

class TestStringsEnglish : TestStrings {
    override val welcome = "Welcome to your test."
    override val solveExercises: String = "Please solve the following exercises:"
    override val askNoOfQuestions: String
        get() = "How many questions would you like for this test?"

    override fun confirmNoOfQuestion(noOfQuestion: Int): String {
        if (noOfQuestion == 1) {
            return "Your test will have $noOfQuestion question."
        }
        return "Your test will have $noOfQuestion questions."
    }

    override val askDifficulty: String
        get() = """
            How difficult should these questions be? 
            You can answer: $difficultyLevels
        """.trimIndent()

    override val difficultyLevels: List<String>
        get() = listOf("Easy", "Medium", "Hard")

    override fun confirmDifficulty(chosenDifficulty: String): String {
        return "Your test will have the difficulty level: $chosenDifficulty"
    }

    override val askToStartTest: String
        get() = "Do you want to start the test?"

    override val testStarts: String
        get() = "The test begins now."

    override val multiplication: String
        get() = "times"
    override val division: String
        get() = "divided by"
    override val percentages: String
        get() = "percent out of"
    override val fractions: String
        get() = "plus"
    override val addition: String
        get() = "plus"
    override val subtraction: String
        get() = "minus"
    override val whatIs: String
        get() = "What is "
    override fun percentage(operandOne: Int, operandTwo: Int): String = "$whatIs$operandOne% of $operandTwo?"
}

class TestStringsDutch : TestStrings {
    override val welcome = "Welkom bij jouw toets:"
    override val solveExercises: String
        get() = "Los de volgende opgaven op:"
    override val askNoOfQuestions: String
        get() = "Hoeveel vragen wil jij oplossen?"

    override fun confirmNoOfQuestion(noOfQuestion: Int): String {
        if (noOfQuestion == 1) {
            return "Jouw toets zal $noOfQuestion vraag bevatten."
        }
        return "Jouw toets zal $noOfQuestion vragen bevatten."
    }

    override val askDifficulty: String
        get() = """Hoe moeilijk moeten de vragen zijn?
            Je kunt kiezen uit: $difficultyLevels
        """.trimMargin()
    override val difficultyLevels: List<String>
        get() = listOf("Makkelijk", "Gemiddeld", "Moeilijk")

    override fun confirmDifficulty(chosenDifficulty: String): String {
        return "De moeilijkheidsgraad van jouw toets is $chosenDifficulty."
    }

    override val askToStartTest: String
        get() = "Wil je nu aan de toets beginnen?"

    override val testStarts: String
        get() = "De toets begint nu."

    override val multiplication: String
        get() = "keer"
    override val division: String
        get() = "gedeeld door"
    override val addition: String
        get() = "plus"
    override val subtraction: String
        get() = "min"
    override val percentages: String
        get() = "percent van"
    override val fractions: String
        get() = "plus"
    override val whatIs: String
        get() = "Wat is "

    override fun percentage(operandOne: Int, operandTwo: Int): String = "$whatIs$operandOne% van $operandTwo?"

}

fun getTestStrings(language: Language): TestStrings {
    return when (language) {
        Language.DUTCH -> TestStringsDutch()
        else -> TestStringsEnglish()
    }
}


fun Furhat.getTestStrings(): TestStrings {
    return when (voice.language) {
        Language.DUTCH -> TestStringsDutch()
        else -> TestStringsEnglish()
    }
}
