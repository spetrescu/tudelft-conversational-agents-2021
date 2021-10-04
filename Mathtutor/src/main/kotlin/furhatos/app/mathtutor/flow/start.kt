package furhatos.app.mathtutor.flow

import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state

val Start: State = state {
    this.onResponse<Name> {
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