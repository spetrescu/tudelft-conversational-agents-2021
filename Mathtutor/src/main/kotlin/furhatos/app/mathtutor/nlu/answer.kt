package furhatos.app.mathtutor.nlu

import furhatos.nlu.Intent
import furhatos.util.Language
import furhatos.nlu.common.Number

class AnswerIntent(val answer: Number = Number(1)) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@answer"
        )
    }
}