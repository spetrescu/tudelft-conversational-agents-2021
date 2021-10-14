package furhatos.app.mathtutor.assessment.quizzes

import furhatos.util.Language
import kotlin.random.Random

class MultiplicationQuiz(
    override val language: Language,
    override val noOfQuestions: Int,
    override val difficulty: String,
    override val previousResults: ArrayList<Result>?
) : AbstractQuiz() {
    override val questions: ArrayList<AbstractQuestion> = generateQuestions()


    override fun generateQuestions(): ArrayList<AbstractQuestion> {
        val questionsArray: ArrayList<AbstractQuestion> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            questionsArray.add(
                MultiplicationQuestion(
                    Random.nextInt(from = 1, until = difficultyLimit),
                    Random.nextInt(from = 1, until = difficultyLimit),
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
            questionsArray.add(
                MultiplicationQuestion(
                    randomGenerator.nextInt(from = 1, until = difficultyLimit),
                    randomGenerator.nextInt(from = 1, until = difficultyLimit),
                    language
                )
            )
        }
        return questionsArray
    }
}