package furhatos.app.mathtutor.flow

import furhatos.flow.kotlin.*
import furhatos.util.Gender
import furhatos.util.Language
import furhatos.app.mathtutor.emotion_handler.EmotionHandler

val emotionHandler: EmotionHandler = EmotionHandler()

val Idle: State = state {

    init {
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE, setInputLanguage = true)
        if (users.count > 0) {
            var currentUser = users.random
            emotionHandler.startEmotionHandler(furhat, currentUser)
            furhat.attend(currentUser)
            goto(Start)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}