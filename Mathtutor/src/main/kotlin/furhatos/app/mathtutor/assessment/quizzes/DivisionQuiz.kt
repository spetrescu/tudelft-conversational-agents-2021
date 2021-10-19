package furhatos.app.mathtutor.assessment.quizzes

import furhatos.util.Language
import kotlin.random.Random

class DivisionQuiz(
    override val language: Language,
    override val noOfQuestions: Int,
    override val difficulty: String,
    override val previousResults: ArrayList<Result>?
) : AbstractQuiz() {
    override val questions: ArrayList<AbstractQuestion> = generateQuestions()

    override fun generateQuestions(): ArrayList<AbstractQuestion> {
        val questionsArray: ArrayList<AbstractQuestion> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            val factorOne: Int = Random.nextInt(from = 1, until = difficultyLimit)
            val factorTwo: Int = Random.nextInt(from = 1, until = difficultyLimit)
            val numerator: Int = factorOne * factorTwo
            val denominator: Int = listOf(factorOne, factorTwo).random()
            questionsArray.add(
                DivisionQuestion(
                    numerator,
                    denominator,
                    0,
                    language
                )
            )
        }
        return questionsArray
    }

    override fun generateQuestions(seed: Int): ArrayList<AbstractQuestion> {
        val questionsArray: ArrayList<AbstractQuestion> = ArrayList(noOfQuestions)
        val randomGenerator = Random(seed)
        (0 until noOfQuestions).forEach { _ ->
            val factorOne: Int = randomGenerator.nextInt(from = 1, until = difficultyLimit)
            val factorTwo: Int = randomGenerator.nextInt(from = 1, until = difficultyLimit)
            val numerator: Int = factorOne * factorTwo
            val denominator: Int = listOf(factorOne, factorTwo).random()
            questionsArray.add(
                DivisionQuestion(
                    numerator,
                    denominator,
                    0,
                    language
                )
            )
        }
        return questionsArray
    }
}