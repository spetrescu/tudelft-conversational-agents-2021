package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.assessment.quizzes.AbstractQuestion
import furhatos.app.mathtutor.assessment.quizzes.Question
import furhatos.app.mathtutor.strings.getGradeStrings
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state

fun grade(questions: ArrayList<AbstractQuestion>): State = state {
    fun gradeCalculation(noOfCorrectlyAnsweredQuestions: Int, noOfQuestions: Int): Long =
        noOfCorrectlyAnsweredQuestions.toLong() / noOfQuestions.toLong() * 9 + 1

    fun calculateGrade(): Long {
        val noOfQuestions: Int = questions.size
        var correctlyAnsweredQuestions = 0
        questions.forEach { question ->
            if (question.getCorrectAnswer() == question.userAnswer!!.answerNumber){
                correctlyAnsweredQuestions += 1
            }
        }
        return gradeCalculation(correctlyAnsweredQuestions, noOfQuestions)
    }

    this.onEntry {
        furhat.say(furhat.getGradeStrings().finishTest)
        furhat.say(furhat.getGradeStrings().checkingAnswers)
        val score = call {
            calculateGrade()
        }
        delay(2500)
        furhat.say(furhat.getGradeStrings().yourScoreIs(score as Long))
        goto(Subject)
    }
}