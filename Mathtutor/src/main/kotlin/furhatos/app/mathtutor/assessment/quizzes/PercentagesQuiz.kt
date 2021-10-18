package furhatos.app.mathtutor.assessment.quizzes

import furhatos.util.Language
import kotlin.random.Random

class PercentagesQuiz(
    override val language: Language,
    override val noOfQuestions: Int,
    override val difficulty: String,
    override val previousResults: ArrayList<Result>?
) : AbstractQuiz() {
    override val questions: ArrayList<AbstractQuestion>
        get() = generateQuestions()

    override fun generateQuestions(): ArrayList<AbstractQuestion> {
        val questionsArray: ArrayList<AbstractQuestion> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            val factorOne = Random.nextInt(from = 1, until = 11) * 10
            val factorTwo: Int = Random.nextInt(from = 1, until = difficultyLimit) * 100
            val firstnumber = factorTwo / 100 * factorOne
            questionsArray.add(
                PercentagesQuestion(
                    firstnumber,
                    factorTwo,
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
            val factorOne = randomGenerator.nextInt(from = 1, until = 11) * 10
            val factorTwo: Int = randomGenerator.nextInt(from = 1, until = difficultyLimit) * 100
            val firstnumber = factorTwo / 100 * factorOne
            questionsArray.add(
                PercentagesQuestion(
                    firstnumber,
                    factorTwo,
                    language
                )
            )
        }
        return questionsArray
    }
}