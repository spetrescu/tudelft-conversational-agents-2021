package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.assessment.quizzes.Question
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state

fun Grade(questions: ArrayList<Question>, answers: ArrayList<Answer>): State = state {
    this.onEntry {
        furhat.say { +"The test has finished." }
    }
}