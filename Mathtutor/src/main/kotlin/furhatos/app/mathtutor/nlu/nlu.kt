package furhatos.app.mathtutor.nlu

import furhatos.app.mathtutor.object_classes.Subject
import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.common.PersonName
import furhatos.util.Language


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