package furhatos.app.mathtutor.language

private val english = object {
    val onResponseFirstResponse = "Sorry, I did not quite get that."
    val onResponseOtherResponses: Array<String> = arrayOf(
        "I'm sorry, I still could not catch that.",
        "Would you mind rephrasing that for me?",
        "I heard something, but I could not comprehend it.",
        "I can hear you, but I won't."
    )
    val onNoResponseFirstResponse = "Sorry, I could not hear you.";
    val onNoResponseOtherResponses: Array<String> = arrayOf(
        "I'm sorry, I am still unable to hear you.",
        "Could you perhaps speak up a little?",
        "I straight up could not hear you, would you mind trying to enunciate?"
    )
    val onResponseFailedResponse = "My apologies, my speech recognizer is out of order. Please try again soon."
}

private val dutch = object {
    val onResponseFirstResponse = "Sorry dat begreep ik niet helemaal."
    val onResponseOtherResponses: Array<String> = arrayOf(
        "Het spijt me, dat heb ik niet kunnen opvangen.",
        "Zou je dat anders kunnen zeggen?",
        "Ik hoorde iets, maar ik kon het niet vatten."
    )
    val onNoResponseFirstResponse = "Sorry, ik kon je niet verstaan.";
    val onNoResponseOtherResponses: Array<String> = arrayOf(
        "Sorry, ik kan je nog steeds niet verstaan.",
        "Zou je iets harder kunnen praten",
        "Ik kan je echt niet horen. Zou je wat duidelijker kunnen praten?"
    )
    val onResponseFailedResponse = "Mijn excuses, mijn spraakherkenning is buiten gebruik. Probeer het later opnieuw."
}

class FallbackStateLanguage(language: String) {

    val onResponseFirstResponse = when(language){
        "Dutch" -> dutch.onResponseFirstResponse
        else -> english.onResponseFirstResponse
    }

    val onResponseOtherResponses = when(language){
        "Dutch" -> dutch.onResponseOtherResponses
        else -> english.onResponseOtherResponses
    }

    val onNoResponseFirstResponse = when(language){
        "Dutch" -> dutch.onNoResponseFirstResponse
        else -> english.onNoResponseFirstResponse
    }

    val onNoResponseOtherResponses = when(language){
        "Dutch" -> dutch.onNoResponseOtherResponses
        else -> english.onNoResponseOtherResponses
    }
}



