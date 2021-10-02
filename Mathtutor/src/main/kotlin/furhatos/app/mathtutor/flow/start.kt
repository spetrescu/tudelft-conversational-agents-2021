package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.*
import furhatos.app.mathtutor.object_classes.UserName
import furhatos.app.mathtutor.object_classes.userName
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Start: State = state(Interaction) {
    onEntry {
        furhat.ask("Hello, I'm your mathtutor, what's your name?")
    }
    onReentry {
        furhat.ask("Can you please repeat your name?")
    }

    onResponse<Name> {
        val name = it.intent.name
        users.current.userName.name = name
        val confirm = furhat.askYN("So, your name is " + it.intent.name +", is that correct?")
        if (confirm == true){
            furhat.gesture(Gestures.Smile, async = true)
            goto(Subject)
        } else{
            furhat.gesture(Gestures.ExpressSad, async = true)
            furhat.say("I'm sorry, I must have misunderstood.")
            reentry()
        }
    }
}