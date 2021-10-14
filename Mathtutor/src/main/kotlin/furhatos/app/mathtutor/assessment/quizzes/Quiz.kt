package furhatos.app.mathtutor.assessment.quizzes

import furhatos.util.Language


interface Quiz {
    val language: Language
    val noOfQuestions: Int
    val difficulty: String
    val previousResults: ArrayList<Result>?
    val questions: ArrayList<AbstractQuestion>
    fun generateQuestions(): ArrayList<AbstractQuestion>
    fun generateQuestions(seed: Int): ArrayList<AbstractQuestion>
}

abstract class AbstractQuiz: Quiz {
    val difficultyLimit: Int
        get() = when (language) {
            Language.DUTCH -> when (difficulty) {
                "Makkelijk" -> 11
                "Gemiddeld" -> 21
                "Moeilijk" -> 101
                else -> 11
            }
            else -> when (difficulty) {
                "Easy" -> 11
                "Medium" -> 21
                "Hard" -> 101
                else -> 11
            }
        }
}

