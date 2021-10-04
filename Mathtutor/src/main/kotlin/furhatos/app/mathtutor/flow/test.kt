package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.assessment.MultiplicationQuiz
import furhatos.app.mathtutor.assessment.Quiz
import furhatos.app.mathtutor.nlu.AnswerIntent
import furhatos.app.mathtutor.nlu.DifficultyLevel
import furhatos.app.mathtutor.nlu.NoIdeaIntent
import furhatos.app.mathtutor.nlu.NoOfQuestionsIntent
import furhatos.app.mathtutor.strings.getTestStrings
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.Yes
import furhatos.util.Language

fun obtainNumberOfQuestions(): State = state(Interaction) {
    var noOfQuestions = (20..31).random()

    this.onEntry {
        furhat.ask(furhat.getTestStrings().askNoOfQuestions)
    }

    this.onResponse<NoOfQuestionsIntent> {
        noOfQuestions = it.intent.numberOfQuestions.getInteger("value")
        furhat.say(furhat.getTestStrings().confirmNoOfQuestion(noOfQuestions))
        terminate(noOfQuestions)
    }

    this.onResponse<NoIdeaIntent> {
        furhat.say(furhat.getTestStrings().confirmNoOfQuestion(noOfQuestions))
        terminate(noOfQuestions)
    }

}

fun obtainDifficultyLevel(): State = state(Interaction) {
    var difficultyLevel: String

    this.onEntry {
        furhat.ask(furhat.getTestStrings().askDifficulty)
    }


    this.onResponse<DifficultyLevel> {
        difficultyLevel = it.intent.difficultyEntity.toString()
        furhat.say(furhat.getTestStrings().confirmDifficulty(difficultyLevel))
        terminate(difficultyLevel)
    }
}

val test: State = state(Interaction) {
    var language: Language? = null
    var noOfQuestions: Int? = null
    var difficultyLevel: String? = null
    var questionNumber = 0
    var quiz: Quiz? = null

    this.onEntry {
        language = furhat.voice.language!!
        furhat.say(furhat.getTestStrings().welcome)
        noOfQuestions = call(obtainNumberOfQuestions()) as Int
        difficultyLevel = call(obtainDifficultyLevel()) as String
        furhat.ask(furhat.getTestStrings().askToStartTest)
    }

    this.onReentry {
        val currentQuestion = quiz?.getQuestions()?.get(questionNumber)
        furhat.ask("${furhat.getTestStrings().whatIs} ${currentQuestion}?")
    }

    this.onResponse<AnswerIntent> {
        println(it.text.toString())
        questionNumber += 1
        if (questionNumber >= quiz?.getQuestions()?.size!!) {
            terminate()
        } else {
            reentry()
        }
    }

    this.onResponse<Yes> {
        furhat.say(furhat.getTestStrings().testStarts)
        quiz = MultiplicationQuiz(language!!, noOfQuestions!!, difficultyLevel!!, null)
        furhat.say(furhat.getTestStrings().solveExercises)
        reentry()
    }
}