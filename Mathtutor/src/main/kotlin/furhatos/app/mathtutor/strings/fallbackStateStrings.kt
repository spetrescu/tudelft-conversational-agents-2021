package furhatos.app.mathtutor.strings

import furhatos.util.Language

interface FallBackStringsInterface: DataStringInterface {
    val onResponseFirstResponse: String
    val onResponseOtherResponses: Array<String>
    val onNoResponseFirstResponse: String
    val onNoResponseOtherResponses: Array<String>
    val onResponseFailedResponse: String
}

class FallbackStringsEnglish: FallBackStringsInterface {
    override val onResponseFirstResponse = "Sorry, I did not quite get that."
    override val onResponseOtherResponses: Array<String> = arrayOf(
        "I'm sorry, I still could not catch that.",
        "Would you mind rephrasing that for me?",
        "I heard something, but I could not comprehend it.",
        "I can hear you, but I won't."
    )
    override val onNoResponseFirstResponse = "Sorry, I could not hear you."
    override val onNoResponseOtherResponses: Array<String> = arrayOf(
        "I'm sorry, I am still unable to hear you.",
        "Could you perhaps speak up a little?",
        "I straight up could not hear you, would you mind trying to enunciate?"
    )
    override val onResponseFailedResponse = "My apologies, my speech recognizer is out of order. Please try again soon."
}

class FallbackStringsDutch: FallBackStringsInterface {
    override val onResponseFirstResponse = "Sorry dat begreep ik niet helemaal."
    override val onResponseOtherResponses: Array<String> = arrayOf(
        "Het spijt me, dat heb ik niet kunnen opvangen.",
        "Zou je dat anders kunnen zeggen?",
        "Ik hoorde iets, maar ik kon het niet vatten."
    )
    override val onNoResponseFirstResponse = "Sorry, ik kon je niet verstaan."
    override val onNoResponseOtherResponses: Array<String> = arrayOf(
        "Sorry, ik kan je nog steeds niet verstaan.",
        "Zou je iets harder kunnen praten",
        "Ik kan je echt niet horen. Zou je wat duidelijker kunnen praten?"
    )
    override val onResponseFailedResponse = "Mijn excuses, mijn spraakherkenning is buiten gebruik. Probeer het later opnieuw."
}

class FallbackStateStrings(private val language: Language?): StringClass() {
    override fun getStrings(): FallBackStringsInterface {
        return when(language){
            Language.DUTCH -> FallbackStringsDutch()
            else -> FallbackStringsEnglish()
        }
    }
}
