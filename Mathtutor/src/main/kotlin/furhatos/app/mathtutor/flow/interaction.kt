package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.Mode
import furhatos.app.mathtutor.nlu.QuestionAnswer
import furhatos.app.mathtutor.nlu.Subjectname
import furhatos.flow.kotlin.*
import furhatos.util.*
import furhatos.app.mathtutor.object_classes.UserName
import furhatos.app.mathtutor.object_classes.Subject
import furhatos.app.mathtutor.object_classes.TrainingMode
import furhatos.gestures.Gestures

var currentsubject = Subject()
var currentMode = TrainingMode()


val Interaction: State = state(FallBackState) {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.say { "Bye bye, see you later" }
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

val Subject: State = state(Interaction){
    onEntry{
        furhat.ask{ "What subject would you like to practice?"}
    }
   onResponse<Subjectname> {
       val subjectanswer = it.intent.subject
       currentsubject.currentSubject = subjectanswer.toString()
       goto(GiveTrainingMode)
   }
}

val GiveTrainingMode: State = state(Interaction){
    onEntry{
        furhat.ask{"Would you like to do a test, practice an example together, get some explanation or try one question?"}
    }
    onResponse<Mode> {
        val modeanswer = it.intent.trainingmode.toString()
        currentMode.currentMode = modeanswer
        if(modeanswer == "test"){
            goto(Test)
        }
        else if(modeanswer == "question"){
            goto(Questions)
        }
        else if(modeanswer == "example"){
            goto(Examples)
        }
        else if(modeanswer == "explanation"){
            goto(Explanation)
        }
        else{
            furhat.say{"I'm sorry, I didn't understand you, can you repeat what you want to do?"}
            reentry()
        }
    }
}

val Test: State = state(Interaction){
    //todo: to be implemented by Rohan
}

val Questions: State = state(Interaction){
    val randomFirstValue = (1..10).random()
    val randomSecondValue = (1..10).random()

    val divisor = (1..10).random()
    val dividend = divisor * randomSecondValue

    val out_of = 100 * (1..10).random()
    val answerp = 10 * (1..10).random()
    val number = out_of / 100 * answerp

    val firstfraction = (1..divisor).random()
    val secondfraction = (1..divisor).random()

    val answerm = randomFirstValue * randomSecondValue
    val answerd = randomFirstValue * randomSecondValue
    val answerf = firstfraction + secondfraction
    onEntry {
        furhat.say { "Let me give you a question!" }
        if (currentsubject.currentSubject == "multiplication") {
            furhat.ask{"What is the answer to " + randomFirstValue.toString() + " multiplied by " + randomSecondValue.toString() + "?"}
        } else if (currentsubject.currentSubject == "division") {
            furhat.ask{"What is the answer to " + dividend.toString() + " divided by " + divisor.toString() + "?"}
        } else if (currentsubject.currentSubject == "percentages") {
            furhat.ask{"What is " + number.toString() + " percent out of " + out_of.toString() + "?"}
        } else if (currentsubject.currentSubject == "fractions") {
            furhat.ask{"What is " + firstfraction.toString() + " above " + divisor.toString() + " plus" + secondfraction.toString() + "above " + divisor.toString() + "? " +
                    "Please give your answer as: number above " + divisor.toString()}
        } else {
            furhat.say { "I'm sorry, the topic isn't clear to me, can you repeat it?" }
            goto(Subject)
        }
    }

    onReentry {
        furhat.ask{"Please repeat your answer."}
    }

    onResponse<QuestionAnswer> {
        onEntry {
            val confirm = furhat.askYN("So, your answer is " + it.intent.givenanswer + ", is that correct?")
            if (confirm == true) {
                if (currentsubject.currentSubject == "multiplication") {
                    if (it.intent.givenanswer == answerm) {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is correct!" }
                    } else {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is wrong! The answer should have been" + answerm.toString() }
                    }
                } else if (currentsubject.currentSubject == "division") {
                    if (it.intent.givenanswer == answerd) {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is correct!" }
                    } else {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is wrong! The answer should have been" + answerd.toString() }
                    }
                } else if (currentsubject.currentSubject == "percentages") {
                    if (it.intent.givenanswer == answerp) {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is correct!" }
                    } else {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is wrong! The answer should have been" + answerp.toString() }
                    }
                } else if (currentsubject.currentSubject == "fractions") {
                    if (it.intent.givenanswer == answerf) {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is correct!" }
                    } else {
                        furhat.say { "Your answer " + it.intent.givenanswer + " is wrong! The answer should have been" + answerf.toString() }
                    }
                } else {
                    furhat.say { "I'm sorry, the topic isn't clear to me, can you repeat it?" }
                    goto(Subject)
                }
            } else {
                furhat.gesture(Gestures.ExpressSad, async = true)
                furhat.say("I'm sorry, I must have misunderstood.")
                reentry()
            }
            goto(GiveTrainingMode)
        }
    }
}

val Examples: State = state(Interaction){
    val randomFirstValue = (1..10).random()
    val randomSecondValue = (1..10).random()

    val divisor = (1..10).random()
    val dividend = divisor * randomSecondValue

    val out_of = 100 * (1..10).random()
    val answerp = 10 * (1..10).random()
    val number = out_of / 100 * answerp

    val firstfraction = (1..divisor).random()
    val secondfraction = (1..divisor).random()

    val answerm = randomFirstValue * randomSecondValue
    val answerd = randomFirstValue * randomSecondValue
    val answerf = (firstfraction + secondfraction).toString() + " above " + divisor.toString()
    onEntry {
        furhat.say { "Let me give you a question!" }
        if (currentsubject.currentSubject == "multiplication") {
            furhat.ask{"What is the answer to " + randomFirstValue.toString() + " multiplied by " + randomSecondValue.toString() + "?" + delay(3000) +
            "The answer is: " + answerm.toString()}
        } else if (currentsubject.currentSubject == "division") {
            furhat.ask{"What is the answer to " + dividend.toString() + " divided by " + divisor.toString() + "?"+ delay(3000) +
                    "The answer is: " + answerd.toString()}
        } else if (currentsubject.currentSubject == "percentages") {
            furhat.ask{"What is " + number.toString() + " percent out of " + out_of.toString() + "?" + delay(3000) +
                    "The answer is: " + answerp.toString()}
        } else if (currentsubject.currentSubject == "fractions") {
            furhat.ask{"What is " + firstfraction.toString() + " above " + divisor.toString() + " plus" + secondfraction.toString() + "above " + divisor.toString() + "? " +
                    "Please give your answer as: number above " + divisor.toString() + "." + delay(3000) +
                    "The answer is: " + answerf.toString()}
        } else {
            furhat.say { "I'm sorry, the topic isn't clear to me, can you repeat it?" }
            goto(Subject)
        }
        goto(GiveTrainingMode)
    }
}

val Explanation: State = state(Interaction){
    onEntry {
        furhat.say{"Alright, you want the topic explained, let's start!"}
        //todo: get better explanations as these are too basic
        if(currentsubject.currentSubject == "multiplication"){
            furhat.say{"The basic idea of multiplication is repeated addition. For example: 5 × 3 = 5 + 5 + 5 = 15." +
                    "We use the × symbol to mean multiply"}
        }
        else if(currentsubject.currentSubject == "division"){
            furhat.say{"Division is splitting into equal parts or groups. It is the result of fair sharing. " +
                    "For example: there are 12 chocolates, and 3 friends want to share them, how do they divide the chocolates?" +
                    "The answer is, they should get 4 each." +
                    "We use the / symbol to mean divide"}
        }
        else if(currentsubject.currentSubject == "percentages"){
            furhat.say{"A percentage means parts per 100" +
                    "The symbol is %" +
                    "For example: 25% means 25 per 100" +
                    "Let's do another example: 10% means 10 out of every 100. So if 10% of 500 people have ice cream, then 50 people have ice cream."}

        }else if(currentsubject.currentSubject == "fractions"){
            furhat.say{"A fraction is how many parts of a whole:" +
                    "the top number (the numerator) says how many parts we have." +
                    "the bottom number (the denominator) says how many equal parts the whole is divided into"}
        }else{
            furhat.say{"I'm sorry, the topic isn't clear to me, can you repeat it?"}
            goto(Subject)
        }
        goto(GiveTrainingMode)
    }
}