package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.language.FallbackStateLanguage
import furhatos.flow.kotlin.*

val FallBackState: State = state {
    var noinput = 0
    var nomatch = 0
    val language = "English"

    onResponse {
        ++nomatch
        if (nomatch == 1) {
            furhat.say(FallbackStateLanguage(language).onResponseFirstResponse)
        } else {
            val responses = FallbackStateLanguage(language).onResponseOtherResponses
            furhat.say(responses.random())
        }
        reentry()
    }

    onNoResponse {
        ++noinput
        if (noinput == 1) {
            furhat.say(FallbackStateLanguage(language).onNoResponseFirstResponse)
        } else {
            val responses = FallbackStateLanguage(language).onNoResponseOtherResponses
            furhat.say(responses.random())
        }
        reentry()
    }

    onResponseFailed {
        furhat.say("My apologies, my speech recognizer is out of order. Please try again soon.")
        terminate()
    }
}