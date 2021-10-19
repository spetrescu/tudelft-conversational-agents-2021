package furhatos.app.mathtutor.assessment.quizzes

import furhatos.util.Language
import kotlin.random.Random

class FractionsQuiz(
    override val language: Language,
    override val noOfQuestions: Int,
    override val difficulty: String,
    override val previousResults: ArrayList<Result>?
) : AbstractQuiz() {
    override val questions: ArrayList<AbstractQuestion> = generateQuestions()


    override fun generateQuestions(): ArrayList<AbstractQuestion> {
        val questionsArray: ArrayList<AbstractQuestion> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            val divisor: Int = Random.nextInt(from = 1, until = 11)
            val factorOne: Int = Random.nextInt(from = 1, until = divisor-1)
            val factorTwo: Int = Random.nextInt(from = 1, until = divisor-1)
            questionsArray.add(
                FractionsQuestion(
                    factorOne,
                    factorTwo,
                    divisor,
                    language
                )
            )
        }
        return questionsArray
    }

    override fun generateQuestions(seed: Int): ArrayList<AbstractQuestion> {
        val randomGenerator = Random(seed)
        val questionsArray: ArrayList<AbstractQuestion> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            val divisor: Int = randomGenerator.nextInt(from = 2, until = 11)
            val factorOne: Int = randomGenerator.nextInt(from = 1, until = divisor-1)
            val factorTwo: Int = randomGenerator.nextInt(from = 1, until = divisor-1)
            questionsArray.add(
                FractionsQuestion(
                    factorOne,
                    factorTwo,
                    divisor,
                    language
                )
            )
        }
        return questionsArray
    }
}