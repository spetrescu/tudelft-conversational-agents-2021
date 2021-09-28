package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.strings.TestStrings
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.util.Language

val test: State = state {
    onEntry {
        TestStrings(Language.ENGLISH_US, "ID", null).getStrings().welcome
    }
}