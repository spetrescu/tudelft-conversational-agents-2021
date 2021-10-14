package furhatos.app.mathtutor.strings

import furhatos.flow.kotlin.Furhat
import furhatos.util.Language

interface GradeStrings {
    val finishTest: String
    val checkingAnswers: String
    fun yourScoreIs(actualScore: Long): String
}

class GradeStringsEnglish: GradeStrings {
    override val finishTest: String
        get() = "Your test has finished"
    override val checkingAnswers: String
        get() = "Checking your answers..."
    override fun yourScoreIs(actualScore: Long): String = "Your score is a ${"%.1f".format(actualScore)} out of 10."
}

class GradeStringsDutch: GradeStrings {
    override val finishTest: String
        get() = "Jouw toets is afgelopen."
    override val checkingAnswers: String
        get() = "Antwoorden nakijken..."

    override fun yourScoreIs(actualScore: Long): String {
        return "Jouw cijfer is een ${"%.1f".format(actualScore)}."
    }
}

fun getGradeStrings(language: Language): GradeStrings {
    return when(language){
        Language.DUTCH -> GradeStringsDutch()
        else -> GradeStringsEnglish()
    }
}

fun Furhat.getGradeStrings(): GradeStrings {
    return when(voice.language){
        Language.DUTCH -> GradeStringsDutch()
        else -> GradeStringsEnglish()
    }
}