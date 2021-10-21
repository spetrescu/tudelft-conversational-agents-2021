package furhatos.app.mathtutor.nlu

import furhatos.nlu.Intent
import furhatos.nlu.common.Number
import furhatos.util.Language

class NoOfQuestionsIntent(
    val numberOfQuestions: Number = Number(1)
) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return when(lang){
            Language.DUTCH -> listOf<String>(
             "Ik wil @numberOfQuestions vragen",
                "@numberOfQuestions alstublieft",
                "@numberOfQuestions alsjeblieft",
                "@numberOfQuestions"
            )
            else -> listOf<String>(
                "@numberOfQuestions",
                "I would like to have @numberOfQuestions",
                "@numberOfQuestions questions please",
                "@numberOfQuestions please",
                "I want @numberOfQuestions",
                "@numberOfQuestions"
            )
        }
    }
}

