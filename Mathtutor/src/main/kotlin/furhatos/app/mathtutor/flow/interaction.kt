package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.Subjectname
import furhatos.flow.kotlin.*
import furhatos.util.*
import furhatos.app.mathtutor.object_classes.UserName
import furhatos.app.mathtutor.object_classes.Subject

var currentsubject = Subject()


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

val Subject = state(Interaction){
    onEntry{
        furhat.ask{ "What subject would you like to practice?"}
    }
   onResponse<Subjectname> {
       val subjectanswer = it.intent.subject
       currentsubject.currentSubject = subjectanswer.toString()
       goto(Learn)
   }
}

val Learn = state(Interaction){
    onEntry{
        furhat.ask{"Would you like to do a test, practice a question together, get some explanation or try one test-question?"}
    }
    //onResponse<Test> {goto(Test)}
    //onResponse<Test-Question> {goto(TestQuestion)}
    //onResponse<Question>{goto(Question)}
    //onResponse<Explanation>{goto(Explanation)}
}
