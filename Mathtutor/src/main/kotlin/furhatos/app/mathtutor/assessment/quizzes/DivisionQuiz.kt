package furhatos.app.mathtutor.assessment.quizzes

import furhatos.util.Language
import kotlin.random.Random

class DivisionQuiz(
    override val language: Language,
    override val noOfQuestions: Int,
    override val difficulty: String,
    override val previousResults: ArrayList<Result>?
) : AbstractQuiz() {
    override val questions: ArrayList<Question>
        get() = generateQuestions()

    override fun generateQuestions(): ArrayList<Question> {
        val questionsArray: ArrayList<Question> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            val factorOne: Int = Random.nextInt(from = 1, until = difficultyLimit)
            val factorTwo: Int = Random.nextInt(from = 1, until = difficultyLimit)
            val numerator: Int = factorOne * factorTwo
            val denominator: Int = listOf(factorOne, factorTwo).random()
            questionsArray.add(
                DivisionQuestion(
                    numerator,
                    denominator,
                    language
                )
            )
        }
        return questionsArray
    }

    override fun generateQuestions(seed: Int): ArrayList<Question> {
        TODO("Not yet implemented")
    }
}