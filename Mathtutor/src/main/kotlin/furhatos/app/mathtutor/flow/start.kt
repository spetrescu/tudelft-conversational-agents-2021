package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.gaze.ConvMode
import furhatos.app.mathtutor.gaze.gazing
import furhatos.app.mathtutor.nlu.Name
import furhatos.app.mathtutor.object_classes.userName
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Start: State = state(Interaction) {
    onEntry {
        furhat.gazing(ConvMode.INTIMACY)
        furhat.ask("Hello, I'm Matthew! What's your name?")
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
            furhat.say("Nice to meet you " + it.intent.name "! Let's start the tutoring session!")
            goto(Subject)
        } else {
            furhat.gesture(Gestures.ExpressSad, async = true)
            furhat.gazing(ConvMode.INTIMACY)
            furhat.say("I'm sorry, I must have misunderstood.")
            reentry()
        }
    }
}
