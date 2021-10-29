package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.assessment.quizzes.AbstractQuestion
import furhatos.app.mathtutor.assessment.quizzes.Question
import furhatos.app.mathtutor.object_classes.currentEmotion
import furhatos.app.mathtutor.gaze.ConvMode
import furhatos.app.mathtutor.gaze.gazing
import furhatos.app.mathtutor.strings.getGradeStrings
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.gestures.Gestures
import furhatos.records.User
import kotlin.math.roundToInt

fun grade(questions: ArrayList<AbstractQuestion>, user: User): State = state {
    fun gradeCalculation(noOfCorrectlyAnsweredQuestions: Int, noOfQuestions: Int): Int =
        (noOfCorrectlyAnsweredQuestions.toFloat() / noOfQuestions.toFloat() * 9 + 1).roundToInt()

    fun calculateGrade(): Int {
        val noOfQuestions: Int = questions.size
        var correctlyAnsweredQuestions = 0
        questions.forEach { question ->
            println("correctanswer:" + question.getCorrectAnswer() + " useranswer: " + question.userAnswer!!.answerNumber)
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
        } as Int
        val currentEmotion = user.currentEmotion.emotion
        print("\n Current user emotion: $currentEmotion")

        delay(2500)
        print(furhat.getGradeStrings().yourScoreIs(score))
        furhat.gazing(ConvMode.INTIMACY)
        if(score >= 6){
            emotionHandler.performGesture(furhat, "Happy")
            furhat.say(furhat.getGradeStrings().yourScoreIs(score))
            emotionHandler.performGesture(furhat, "Confirm")
            furhat.say("Congratulations! You passed the test. ")
            if (!currentEmotion.equals("Happy")) {
                emotionHandler.performGesture(furhat, "Uplifting")
                furhat.say("Give yourself some credit, you did a great job.")
            }
        }else{
            if (currentEmotion.equals("Sad") && user.currentEmotion.polarity <= 0.0f) {
                emotionHandler.performGesture(furhat, "Encouraging")
                furhat.say(furhat.getGradeStrings().yourScoreIs(score))
                furhat.say("Unfortunately you didn't pass the test this time.")
                emotionHandler.performGesture(furhat, "Uplifting")
                furhat.say("But don't worry! We can practice a bit more.")
            } else if (currentEmotion.equals("Frustrated")  && user.currentEmotion.polarity <= 0.0f) {
                emotionHandler.performGesture(furhat, "Calming")
                furhat.say(furhat.getGradeStrings().yourScoreIs(score))
                furhat.say("I'm sorry you didn't pass the test this time.")
                emotionHandler.performGesture(furhat, "Uplifting")
                furhat.say("Let's practice a bit more.")
            } else{
                emotionHandler.performGesture(furhat, "Encouraging")
                furhat.say(furhat.getGradeStrings().yourScoreIs(score))
                furhat.say("You didn't pass this time. Let's practice some more.")
            }
        }
        emotionHandler.performGesture(furhat, "Neutral")
        goto(Subject)
    }
}