package furhatos.app.mathtutor.nlu

import furhatos.nlu.Intent
import furhatos.util.Language

class ChangeLanguage: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf<String>(
            "Ja",
            "Yes",
            "Aub",
            "Ajb",
            "Alstublieft",
            "Alsjeblieft",
            "Please"
        )
    }
}

class DontChangeLanguage: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf<String>(
            "Nee",
            "No",
            "Nee, bedankt",
            "Nee, dankuwel",
            "Liever niet",
            "No thanks"
        )
    }
}