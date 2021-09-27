package furhatos.app.mathtutor.flow

import furhatos.flow.kotlin.*
import furhatos.util.*

val Idle: State = state {

    init {
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
        if (users.count > 0) {
            furhat.attend(users.random)
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

val FallBackState: State = state {
    var noinput = 0
    var nomatch = 0

    onResponse {
        ++nomatch
        if (nomatch == 1) {
            furhat.say("Sorry, I did not quite get that.")
        } else {
            val responses: Array<String> = arrayOf(
                "I'm sorry, I still could not catch that.",
                "Would you mind rephrasing that for me?",
                "I heard something, but I could not comprehend it.",
                "I can hear you, but I won't."
            )
            furhat.say(responses.random())
        }
        reentry()
    }

    onNoResponse {
        ++noinput
        if (noinput == 1) {
            furhat.say("Sorry, I could not hear you.")
        } else {
            val responses: Array<String> = arrayOf(
                "I'm sorry, I am still unable to hear you.",
                "Could you perhaps speak up a little?",
                "I straight up could not hear you, would you mind trying to enunciate?"
            )
            furhat.say(responses.random())
        }
        reentry()
    }

    onResponseFailed {
        furhat.say("My apologies, my speech recognizer is out of order. Please try again soon.")
        terminate()
    }
}



val Interaction: State = state(FallBackState) {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }

}

