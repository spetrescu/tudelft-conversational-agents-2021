package furhatos.app.mathtutor.assessment

import furhatos.app.mathtutor.strings.getTestStrings
import furhatos.util.Language
import kotlin.random.Random

class MultiplicationQuiz(
    override val language: Language,
    override val noOfQuestions: Int,
    override val difficulty: String,
    override val previousResults: ArrayList<Result>?
) : Quiz {

    private val difficultyLimit: Int
        get() = when (language) {
            Language.DUTCH -> when (difficulty) {
                "Easy" -> 11
                "Medium" -> 21
                "Hard" -> 101
                else -> 11
            }
            else -> when (difficulty) {
                "Makkelijk" -> 11
                "Gemiddeld" -> 21
                "Moeilijk" -> 101
                else -> 11
            }
        }

    override fun getQuestions(): ArrayList<String> {
        val questionsArray: ArrayList<String> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            questionsArray.add(
                Random.nextInt(from = 1, until = difficultyLimit).toString()
                    .plus(" ${getTestStrings(language).multiplication} ")
                    .plus(Random.nextInt(from = 1, until = difficultyLimit).toString())
            )
        }
        return questionsArray
    }

    override fun getQuestions(seed: Int): ArrayList<String> {
        val randomGenerator = Random(seed)
        val questionsArray: ArrayList<String> = ArrayList(noOfQuestions)
        (0 until noOfQuestions).forEach { _ ->
            questionsArray.add(
                randomGenerator.nextInt(from = 1, until = difficultyLimit).toString()
                    .plus(" ${getTestStrings(language).multiplication} ")
                    .plus(randomGenerator.nextInt(from = 1, until = difficultyLimit).toString())
            )
        }
        return questionsArray
    }
}

interface Quiz {
    val language: Language
    val noOfQuestions: Int
    val difficulty: String
    val previousResults: ArrayList<Result>?
    fun getQuestions(): ArrayList<String>
    fun getQuestions(seed: Int): ArrayList<String>
}