package furhatos.app.mathtutor.nlu



import furhatos.app.mathtutor.strings.getTestStrings
import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.TextGenerator
import furhatos.util.Language

class DifficultyEntity : EnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return getTestStrings(lang).difficultyLevels
    }
}



class DifficultyLevel(val difficultyEntity: DifficultyEntity? = null) : Intent(), TextGenerator {
    override fun getExamples(lang: Language): List<String> {
        return when (lang) {
            Language.DUTCH -> listOf(
                "@difficultyEntity graag",
                "@difficultyEntity alstublieft",
                "@difficultyEntity alsjeblieft",
                "Ik wil graag een @difficultyEntity toets",
                "Ik wil een @difficultyEntity toets aub"
            )
            else -> listOf(
                "@difficultyEntity",
                "@difficultyEntity please",
                "I would like a @difficultyEntity test please",
                "A @difficultyEntity test please"
            )
        }
    }

    override fun toText(lang: Language?): String {
        return "$difficultyEntity"
    }

    override fun toString(): String {
        return toText()
    }


}
