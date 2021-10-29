package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.gaze.ConvMode
import furhatos.app.mathtutor.gaze.gazing
import furhatos.app.mathtutor.nlu.Name
import furhatos.app.mathtutor.object_classes.userName
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

/**
 * Every utterance of Furhat (i.e. furhat.say())
 * is preceded by a method call that sets the emotion of MATHew
 * followed by a method call that triggers a temporary gaze aversion
 */

val Start: State = state(Interaction) {
    onEntry {
        emotionHandler.performGesture(furhat, "Happy")
        furhat.gazing(ConvMode.INTIMACY)
        furhat.say("Hello!")
        delay(500)
        emotionHandler.performGesture(furhat, "Neutral")
        furhat.ask("I'm Matthew! What's your name?")

    }

    onReentry {
        furhat.gazing(ConvMode.INTIMACY)
        furhat.ask("Can you please repeat your name?")
    }

    this.onResponse<Name> {
        val name = it.intent.name
        users.current.userName.name = name
        furhat.gazing(ConvMode.COGNITIVE)
        val confirm = furhat.askYN("So, your name is " + it.intent.name + ", is that correct?")
        if (confirm == true) {
            furhat.gesture(Gestures.Smile, async = true)
            furhat.say("Nice to meet you " + it.intent.name + "! Let's start the tutoring session!")
            goto(Subject)
        } else {
            emotionHandler.performGesture(furhat, "Encouraging")
            furhat.gazing(ConvMode.INTIMACY)
            furhat.say("I'm sorry, I must have misunderstood.")
            emotionHandler.performGesture(furhat, "Neutral")
            reentry()
        }
    }
}
