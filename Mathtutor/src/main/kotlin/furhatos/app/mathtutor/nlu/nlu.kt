package furhatos.app.mathtutor.nlu

import furhatos.nlu.Intent
import furhatos.util.Language
import furhatos.nlu.common.Number


// CheckIn Intent
class CheckIn : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I would like to check in", "I would like to check-in",
                "check-in",
                "checkin"
        )
    }
}

// Wtf is going on intent
class Confused : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Who are you?", "Where am I?", "What?", "What is this?", "What the hell is this?")
    }
}

class TrainingMode : EnumEntity(){
    override fun getEnum(lang: Language): List<String> {
        return listOf("test", "question", "example", "explanation")
    }
}

class Mode : Intent(){
    var trainingmode : TrainingMode? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@trainingmode please",
            "I would like to do a @trainingmode",
            "I would like to do an @trainingmode",
            "I would like a @trainingmode",
            "I would like an @trainingmode",
            "@trainingmode",
            "@trainingmode me",
            "Give me a @trainingmode, please",
            "Give me a @trainingmode",
            "I want a @trainingmode",
            "I want a @trainingmode, please"
        )
    }
}

class QuestionAnswer: Intent(){
    var givenanswer: Number = Number(1)
    var divisor: Number = Number(1)
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@givenanswer",
            "the answer is @givenanswer",
            "@givenanswer percent",
            "the answer is @givenanswer percent",
            "@givenanswer above @divisor",
            "the answer is @givenanswer above @divisor"
        )
    }
}
