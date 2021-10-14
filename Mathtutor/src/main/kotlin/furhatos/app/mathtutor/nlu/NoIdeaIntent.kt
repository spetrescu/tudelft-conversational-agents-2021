package furhatos.app.mathtutor.nlu

import furhatos.nlu.Intent
import furhatos.util.Language

class NoIdeaIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return when(lang){
            Language.DUTCH -> listOf<String>(
                "Geen idee",
                "Geen flauw idee",
                "Zou ik niet weten",
                "Weet ik niet"
            )
            else -> listOf<String>(
                "No idea",
                "No clue",
                "I am clueless",
                "I don't know",
                "I do not know",
                "Dunno",
                "Don't know",
                "Don't care"
            )
        }
    }
}