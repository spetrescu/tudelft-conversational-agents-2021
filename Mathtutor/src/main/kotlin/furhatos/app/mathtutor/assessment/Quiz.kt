package furhatos.app.mathtutor.assessment

import furhatos.util.Language

class MultiplicationQuiz(nameOrId: String, language: Language, previousResults: Collection<Result>?) :
    Quiz {
    override fun getQuestions(seed: Int?): List<String> {
        TODO("Not yet implemented")
    }

}

interface Quiz {
    fun getQuestions(seed: Int?): List<String>
}