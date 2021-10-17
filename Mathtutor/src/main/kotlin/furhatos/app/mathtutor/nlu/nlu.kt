package furhatos.app.mathtutor.nlu

import furhatos.app.mathtutor.object_classes.Subject
import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.common.PersonName
import furhatos.util.Language
import furhatos.nlu.common.Number


// Wtf is going on intent
class Confused : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Who are you?", "Where am I?", "What?", "What is this?", "What the hell is this?")
    }
}

class Name(
    val greeting : Greeting? = null
): Intent(){
    var name: PersonName? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@name",
            "My name is @name.",
            "I am @name",
            "I'm @name",
            "@greeting, my name is @name.",
            "@greeting, I am @name",
            "@greeting, I'm @name"
        )
    }
}

class Greeting : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("hello", "hi", "greetings", "howdy")
    }
}

class Subjectlist : EnumEntity(){
    override fun getEnum(lang: Language): List<String> {
        return listOf("multiplication", "division", "percentages", "fractions")
    }
}

class Subjectname : Intent(){
    var subject : Subjectlist? = null
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@subject please",
            "@subject"
        )
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

class Back : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Go back please",
            "I would like to go back",
            "I would like to go to subject selection",
            "Go back",
            "Back",
            "Subject selection",
            "@trainingmode me"
        )
    }
}

class ExitProgram : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Leave please",
            "End",
            "I would like to go stop",
            "Stop",
            "Please end this",
            "Leave",
            "Please end",
            "I want to leave"
        )
    }
}

class SayYes : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "yes",
            "yeah",
            "Yes, I want to leave",
            "I want to leave",
            "Affirmative",
            "Please let me leave"
        )
    }
}

class SayNo : Intent(){
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "no",
            "I don't",
            "No, I don't"
        )
    }
}
