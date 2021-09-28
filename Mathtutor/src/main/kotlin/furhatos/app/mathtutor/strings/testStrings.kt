package furhatos.app.mathtutor.strings

import furhatos.app.mathtutor.assessment.*
import furhatos.util.Language

interface TestStringsInterface : DataStringInterface {
    val welcome: String
    val questions: List<String>
}

class TestStringsEnglish(nameOrId: String, previousResults: Collection<Result>?) : TestStringsInterface {
    override val welcome = "Welcome to your test."
    override val questions: List<String> =
        MultiplicationQuiz(nameOrId, Language.ENGLISH_US, previousResults).getQuestions(null)
}

class TestStringsDutch(nameOrId: String, previousResults: Collection<Result>?) : TestStringsInterface {
    override val welcome = "Welkom bij jouw toets"
    override val questions: List<String> =
        MultiplicationQuiz(nameOrId, Language.ENGLISH_US, previousResults).getQuestions(null)

}

class TestStrings(
    private val language: Language?,
    private val nameOrId: String,
    private val previousResults: Collection<Result>?
) : StringClass() {
    override fun getStrings(): TestStringsInterface {
        return when (language) {
            Language.DUTCH -> TestStringsDutch(nameOrId, previousResults)
            else -> TestStringsEnglish(nameOrId, previousResults)
        }
    }

}