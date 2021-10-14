package furhatos.app.mathtutor.assessment.quizzes

import furhatos.util.Language
import kotlin.random.Random

class PercentageQuiz(
    override val language: Language,
    override val noOfQuestions: Int,
    override val difficulty: String,
    override val previousResults: ArrayList<Result>?,
    override val questions: ArrayList<AbstractQuestion>
) : AbstractQuiz() {
    override fun generateQuestions(): ArrayList<AbstractQuestion> {
        val questionsArray: ArrayList<AbstractQuestion> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            val factorOne: Int = Random.nextInt(from = 1, until = difficultyLimit.coerceAtMost(100))
            val factorTwo: Int = Random.nextInt(from = 1, until = difficultyLimit)
            val wholeNumber: Int = factorOne * factorTwo
            questionsArray.add(
                PercentageQuestion(
                    factorOne,
                    wholeNumber,
                    language
                )
            )
        }
        return questionsArray
    }

    override fun generateQuestions(seed: Int): ArrayList<AbstractQuestion> {
        TODO("Not yet implemented")
    }
}