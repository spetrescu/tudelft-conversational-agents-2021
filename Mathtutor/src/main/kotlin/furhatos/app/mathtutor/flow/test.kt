package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.assessment.quizzes.*
import furhatos.app.mathtutor.nlu.*
import furhatos.app.mathtutor.object_classes.Subject
import furhatos.app.mathtutor.object_classes.TrainingMode
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

val Test: State = state(Interaction) {
    var language: Language? = null
    var noOfQuestions: Int? = null
    var difficultyLevel: String? = null
    var questionNumber = 0
    var quiz: Quiz? = null
    var currentQuestion: AbstractQuestion? = null

    fun getSubjectQuiz(): Quiz {
        return when (currentsubject.currentSubject) {
            "multiplication" -> MultiplicationQuiz(language!!, noOfQuestions!!, difficultyLevel!!, null)
            "division" -> DivisionQuiz(language!!, noOfQuestions!!, difficultyLevel!!, null)
//            "percentages" ->
//            "fractions" ->
            else -> throw Error("Subject has not been chosen!")
        }
    }


    this.onEntry {
        furhat.say(furhat.getTestStrings().welcome)
        language = furhat.voice.language
        noOfQuestions = call(obtainNumberOfQuestions()) as Int
        difficultyLevel = call(obtainDifficultyLevel()) as String
        furhat.ask(furhat.getTestStrings().askToStartTest)
    }

    this.onReentry {
        currentQuestion = quiz?.questions?.get(questionNumber)
        furhat.ask("${furhat.getTestStrings().whatIs} ${currentQuestion}?")
    }

    this.onResponse<AnswerIntent> {
        currentQuestion?.userAnswer = Answer(it.intent.answer.get("value") as Int)
        questionNumber += 1
        if (questionNumber >= noOfQuestions!!) {
            goto(grade(quiz!!.questions))
        } else {
            reentry()
        }
    }

    this.onResponse<Yes> {
        furhat.say(furhat.getTestStrings().testStarts)
        quiz = MultiplicationQuiz(furhat.voice.language!!, noOfQuestions!!, difficultyLevel!!, null)
        furhat.say(furhat.getTestStrings().solveExercises)
        reentry()
    }
}