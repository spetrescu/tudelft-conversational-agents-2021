package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.Name
import furhatos.app.mathtutor.object_classes.userName
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Start: State = state(Interaction) {
    onEntry {
        emotionHandler.performGesture(furhat, "Happy")
        furhat.say("Hello!")
        delay(500)
        emotionHandler.performGesture(furhat, "Neutral")
        furhat.ask("I'm your Math tutor. What's your name?")
    }
    onReentry {
        furhat.ask("Can you please repeat your name?")
    }

    this.onResponse<Name> {
        val name = it.intent.name
        users.current.userName.name = name
        val confirm = furhat.askYN("So, your name is " + it.intent.name + ", is that correct?")
        if (confirm == true) {
            furhat.gesture(Gestures.Smile, async = true)
            goto(Subject)
        } else {
            emotionHandler.performGesture(furhat, "Encouraging")
            furhat.say("I'm sorry, I must have misunderstood.")
            emotionHandler.performGesture(furhat, "Neutral")
            reentry()
        }
    }
}