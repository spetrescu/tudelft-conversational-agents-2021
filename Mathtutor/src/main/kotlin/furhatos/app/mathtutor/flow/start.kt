package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.ChangeLanguage
import furhatos.app.mathtutor.nlu.DontChangeLanguage
import furhatos.flow.kotlin.*
import furhatos.util.Gender
import furhatos.util.Language

val Start: State = state {
     this.onEntry {
         furhat.say("Do you want to change the language into Dutch?")
         furhat.setVoice(Language.DUTCH, Gender.MALE)
         furhat.ask("Wil je de taal aanpassen naar het Nederlands?")
         furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
     }

    this.onResponse<ChangeLanguage> {
        furhat.setVoice(Language.DUTCH, Gender.MALE)
        furhat.say("Vanaf nu spreek ik Nederlands.")
        terminate()
    }

    this.onResponse<DontChangeLanguage> {
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
        furhat.say("From now on I will be speaking English.")
        terminate()
    }
}