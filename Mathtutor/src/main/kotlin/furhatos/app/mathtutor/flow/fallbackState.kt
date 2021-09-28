package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.strings.FallbackStateStrings
import furhatos.flow.kotlin.*
import furhatos.util.Language


val FallBackState: State = state {
    var noinput = 0
    var nomatch = 0


    onResponse {
        ++nomatch
        if (nomatch == 1) {
            furhat.say(FallbackStateStrings(furhat.voice.language).getStrings().onResponseFirstResponse)
        } else {
            val responses = FallbackStateStrings(furhat.voice.language).getStrings().onResponseOtherResponses
            furhat.say(responses.random())
        }
        reentry()
    }

    onNoResponse {
        ++noinput
        if (noinput == 1) {
            furhat.say(FallbackStateStrings(furhat.voice.language).getStrings().onNoResponseFirstResponse)
        } else {
            val responses = FallbackStateStrings(furhat.voice.language).getStrings().onNoResponseOtherResponses
            furhat.say(responses.random())
        }
        reentry()
    }

    onResponseFailed {
        furhat.say("My apologies, my speech recognizer is out of order. Please try again soon.")
        terminate()
    }
}