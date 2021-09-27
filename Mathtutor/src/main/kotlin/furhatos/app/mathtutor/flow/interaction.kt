package furhatos.app.mathtutor.flow

import furhatos.flow.kotlin.*
import furhatos.util.*



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

