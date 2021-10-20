package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.Mode
import furhatos.app.mathtutor.nlu.QuestionAnswer
import furhatos.app.mathtutor.nlu.Subjectlist
import furhatos.app.mathtutor.nlu.Subjectname
import furhatos.app.mathtutor.nlu.SayNo
import furhatos.app.mathtutor.nlu.SayYes
import furhatos.app.mathtutor.nlu.Back
import furhatos.app.mathtutor.nlu.ExitProgram

import furhatos.app.mathtutor.object_classes.Subject
import furhatos.app.mathtutor.object_classes.TrainingMode
import furhatos.flow.kotlin.*
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
                furhat.say("Bye bye, see you later")
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

val Leave : State = state(Interaction){
    onEntry {
        furhat.ask("Do you really want to stop the lesson?")
    }
    this.onResponse<SayYes>{
        furhat.say("See you later!")
        goto(Idle)
    }
    this.onResponse<SayNo>{
        furhat.say("Let's get back to where we were!")
        terminate()
    }
}

val Subject: State = state(Interaction) {
    onEntry {
        furhat.ask("What subject would you like to practice? You can choose between ${Subjectlist().getEnum(furhat.voice.language!!)}")
    }
    this.onResponse<Subjectname> {
        val answeredSubject = it.intent.subject
        currentsubject.currentSubject = answeredSubject.toString()
        goto(GiveTrainingMode)
    }
    this.onResponse<ExitProgram>{
        call(Leave)
        reentry()
    }
}

val GiveTrainingMode: State = state(Interaction) {
    onEntry {
        furhat.ask(
            "Would you like to do a test, practice an example together, get some explanation or try one question?" +
                    " You can also go back to the subject selection if you want."
        )
    }
    this.onResponse<Mode> {
        val answeredMode = it.intent.trainingmode.toString()
        currentMode.currentMode = answeredMode
        when (answeredMode) {
            "test" -> goto(Test)
            "question" -> goto(Questions)
            "example" -> goto(Examples)
            "explanation" -> goto(Explanation)
            else -> {
                furhat.say("I'm sorry, I didn't understand you, can you repeat what you want to do?")
                reentry()
            }
        }
    }
    this.onResponse<Back>{
        goto(Subject)
    }
    this.onResponse<ExitProgram>{
        call(Leave)
        reentry()
    }
}

val Questions: State = state(Interaction) {
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
    val answerd = randomSecondValue
    val answerf = firstfraction + secondfraction
    onEntry {
        furhat.say("Let me give you a question!")
        when (currentsubject.currentSubject) {
            "multiplication" -> furhat.ask("What is the answer to $randomFirstValue multiplied by ${randomSecondValue}?")
            "division" -> furhat.ask("What is the answer to $dividend divided by $divisor?")
            "percentages" -> furhat.ask("What is $number percent out of $out_of?")
            "fractions" -> furhat.ask(
                "What is " + firstfraction.toString() + " above " + divisor.toString() + " plus" + secondfraction.toString() + "above " + divisor.toString() + "? " +
                        "Please give your answer as: number above " + divisor.toString()
            )
            else -> {
                furhat.say("I'm sorry, the topic isn't clear to me, can you repeat it?")
                goto(Subject)
            }
        }
    }

    onReentry {
        furhat.ask("Please repeat your answer.")
    }

    this.onResponse<QuestionAnswer> {
        val confirm = furhat.askYN("So, your answer is " + it.intent.givenanswer + ", is that correct?")

        if (confirm == true) {
            val correctAnswer: Int = when (currentsubject.currentSubject) {
                "multiplication" -> answerm
                "division" -> answerd
                "percentages" -> answerp
                "fractions" -> answerf
                else -> answerm
            }
            val givenAnswer = it.intent.givenanswer.getInteger("value")
            val isAnswerCorrect = givenAnswer == correctAnswer
            if (isAnswerCorrect) {
                // TODO add emotion behavior
                furhat.say("Your answer " + it.intent.givenanswer + " is correct!")
            } else {
                // TODO add emotion behavior
                furhat.say("Your answer " + it.intent.givenanswer + " is wrong! The answer should have been " + correctAnswer.toString())
            }
        } else {
            furhat.gesture(Gestures.ExpressSad, async = true)
            furhat.say("I'm sorry, I must have misunderstood.")
            reentry()
        }
        goto(GiveTrainingMode)
    }

    this.onResponse<ExitProgram>{
        call(Leave)
        reentry()
    }


}

val Examples: State = state(Interaction) {
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
    val answerd = randomSecondValue
    val answerf = (firstfraction + secondfraction).toString() + " above " + divisor.toString()
    onEntry {
        furhat.say("Let me give you an example question!")
        when (currentsubject.currentSubject) {
            "multiplication" -> {
                furhat.say {
                    +"What is the answer to $randomFirstValue multiplied by $randomSecondValue? "
                    +delay(3000)
                    +"The answer is: $answerm"
                }
            }
            "division" -> {
                furhat.say {
                    +"What is the answer to $dividend divided by $divisor? "
                    +delay(3000)
                    +"The answer is: $answerd"
                }
            }
            "percentages" -> {
                furhat.say {
                    +"What is $number percent out of $out_of? "
                    +delay(3000)
                    +"The answer is: $answerp"
                }
            }
            "fractions" -> {
                furhat.say {
                    +"What is $firstfraction above $divisor plus $secondfraction above $divisor? "
                    +"Please give your answer as: number above $divisor. "
                    +delay(3000)
                    +"The answer is: $answerf."
                }
            }
            else -> {
                furhat.say("I'm sorry, the topic isn't clear to me, can you repeat it?")
                goto(Subject)
            }
        }
        goto(GiveTrainingMode)
    }
    this.onResponse<ExitProgram>{
        call(Leave)
        reentry()
    }
}

val Explanation: State = state(Interaction) {
    onEntry {
        furhat.say("Alright, you want the topic explained, let's start!")
        //todo: get better explanations as these are too basic
        when (currentsubject.currentSubject) {
            "multiplication" -> {
                furhat.say(
                    "The basic idea of multiplication is repeated addition. For example: 5 × 3 = 5 + 5 + 5 = 15." +
                            "We use the × symbol to mean multiply"
                )
            }
            "division" -> {
                furhat.say(
                    "Division is splitting into equal parts or groups. It is the result of fair sharing. " +
                            "For example: there are 12 chocolates, and 3 friends want to share them, how do they divide the chocolates?" +
                            "The answer is, they should get 4 each." +
                            "We use the / symbol to mean divide"
                )
            }
            "percentages" -> {
                furhat.say(
                    "A percentage means parts per 100" +
                            "The symbol is %" +
                            "For example: 25% means 25 per 100" +
                            "Let's do another example: 10% means 10 out of every 100. So if 10% of 500 people have ice cream, then 50 people have ice cream."
                )

            }
            "fractions" -> {
                furhat.say(
                    "A fraction is how many parts of a whole:" +
                            "the top number (the numerator) says how many parts we have." +
                            "the bottom number (the denominator) says how many equal parts the whole is divided into"
                )
            }
            else -> {
                furhat.say("I'm sorry, the topic isn't clear to me, can you repeat it?")
                goto(Subject)
            }
        }
        goto(GiveTrainingMode)
    }
}