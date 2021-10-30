package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.gaze.ConvMode
import furhatos.app.mathtutor.gaze.gazing
import furhatos.app.mathtutor.nlu.ExitProgram
import furhatos.app.mathtutor.nlu.Name
import furhatos.app.mathtutor.nlu.NluNlgBranch
import furhatos.app.mathtutor.object_classes.currentEmotion
import furhatos.app.mathtutor.object_classes.currentResponse
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
        print(" Starting emo handler")
        emotionHandler.startEmotionHandler(users.current)
        print(" Emo handler started!")

        emotionHandler.serverNluNlg(furhat, users.current)

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

    this.onResponse<NluNlgBranch> {
        furhat.say("You chose the Natural Language Processing Branch!")
        goto(FirstUtterance)
    }

    this.onResponse<Name> {
        print(it.text)
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
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

val FirstUtterance: State = state(Interaction) {
    onEntry {
        furhat.ask("Tell something to our agent!")
    }

    this.onResponse {
        emotionHandler.clientNluNlg(it.text)
        furhat.say("Now waiting for answer from our system... answer from our system")
        goto(ReceiveResponse)
    }
}

val Intermediary: State = state(Interaction) {

    onEntry {
        delay(2000)
        val currentResponse = furhat.users.current.currentResponse.response
        print("onEntryIntermediary\n")
        print("system: $currentResponse\n")
        furhat.ask(currentResponse)
    }

    onReentry {
        delay(2000)
        val currentResponse = furhat.users.current.currentResponse.response
        print("onReentryIntermediary\n")
        print("system: $currentResponse\n")
        furhat.ask(currentResponse)
    }

    this.onResponse {
        emotionHandler.clientNluNlg(it.text)
        goto(ReceiveResponse)
    }

    this.onResponse<ExitProgram> {
        furhat.say("See you later!")
        goto(Idle)
    }

}


val ReceiveResponse: State = state(Interaction) {

    onEntry {
        delay(2000)
        //var currentResponse = users.current.currentResponse.response
        val currentResponse = furhat.users.current.currentResponse.response
        print("onEntryReceiveResponse\n")
        print("system: $currentResponse\n")
        furhat.ask(currentResponse)
    }

    onReentry {
        delay(2000)
        val currentResponse = furhat.users.current.currentResponse.response
        print("onReentryReceiveResponsey\n")
        print("system: $currentResponse\n")
        furhat.ask(currentResponse)
    }

    this.onResponse {
        emotionHandler.clientNluNlg(it.text)
        goto(Intermediary)
    }

    this.onResponse<ExitProgram> {
        furhat.say("See you later!")
        goto(Idle)
    }

}
