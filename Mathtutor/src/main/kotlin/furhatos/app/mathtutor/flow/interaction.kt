package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.gaze.ConvMode
import furhatos.app.mathtutor.gaze.gazing
import furhatos.app.mathtutor.nlu.*
import furhatos.app.mathtutor.object_classes.Subject
import furhatos.app.mathtutor.object_classes.TrainingMode
import furhatos.app.mathtutor.object_classes.currentEmotion
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.Voice
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
                furhat.gazing(ConvMode.INTIMACY)
                furhat.say("Bye bye, see you later")
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.gazing(ConvMode.INTIMACY)
        furhat.glance(it)
    }

}

/**
 * Leaving MATHew is handled here
 */
val Leave: State = state(Interaction) {
    onEntry {
        furhat.gazing(ConvMode.TURNTAKING)
        furhat.ask("Do you really want to stop the lesson?")
    }
    this.onResponse<SayYes>{
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        furhat.gazing(ConvMode.INTIMACY)
        furhat.say("See you later!")
        goto(Idle)
    }
    this.onResponse<SayNo>{
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        furhat.gazing(ConvMode.INTIMACY)
        furhat.say("Let's get back to where we were!")
        terminate()
    }
}

/**
 * The subject selection takes place in this state
 */
val Subject: State = state(Interaction) {
    onEntry {
        furhat.ask(
            "What subject would you like to practice? You can choose between ${
                furhat.voice.emphasis(
                    Subjectlist().getEnum(furhat.voice.language!!)
                        .toString(), Voice.EmphasisLevel.MODERATE
                )
            }"
        )
    }
    this.onResponse<Subjectname> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        val answeredSubject = it.intent.subject
        currentsubject.currentSubject = answeredSubject.toString()
        goto(GiveTrainingMode)
    }
    this.onResponse<ExitProgram> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        call(Leave)
        reentry()
    }
}

/**
 * Here, the user gets to select how they want to be tutored for the subject selection of choice
 */
val GiveTrainingMode: State = state(Interaction) {
    onEntry {
        furhat.gazing(ConvMode.TURNTAKING)
        furhat.ask(
            "Would you like to do a test, practice an example together, get some explanation or try one question?" +
                    " You can also go back to the subject selection if you want."
        )

    }
    this.onResponse<Mode> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        val answeredMode = it.intent.trainingmode.toString()
        currentMode.currentMode = answeredMode
        when (answeredMode) {
            "test" -> goto(Test)
            "question" -> goto(Questions)
            "example" -> goto(Examples)
            "explanation" -> goto(Explanation)
            else -> {
                furhat.gazing(ConvMode.TURNTAKING)
                furhat.say("I'm sorry, I didn't understand you, can you repeat what you want to do?")
                reentry()
            }
        }
    }
    this.onResponse<Back> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        goto(Subject)
    }
    this.onResponse<ExitProgram> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        call(Leave)
        reentry()
    }
}

/**
 * Questions are generated in this state
 */
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

    val correctAnswer: Int = when (currentsubject.currentSubject) {
        "multiplication" -> answerm
        "division" -> answerd
        "percentages" -> answerp
        "fractions" -> answerf
        else -> answerm
    }
    onEntry {
        furhat.gazing(ConvMode.INTIMACY)
        furhat.say("Let me give you a question!")
        furhat.gazing(ConvMode.TURNTAKING)
        when (currentsubject.currentSubject) {
            "multiplication" -> furhat.ask("What is the answer to $randomFirstValue multiplied by ${randomSecondValue}?")
            "division" -> furhat.ask("What is the answer to $dividend divided by $divisor?")
            "percentages" -> furhat.ask("How much percent is $number out of $out_of?")
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
        furhat.gazing(ConvMode.TURNTAKING)
        furhat.ask("Please repeat your answer.")
    }

    this.onResponse<QuestionAnswer> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        furhat.gazing(ConvMode.TURNTAKING)
        val confirm = furhat.askYN("So, your answer is " + it.intent.givenanswer + ", is that correct?")

        if (confirm == true) {
            val givenAnswer = it.intent.givenanswer.getInteger("value")
            val isAnswerCorrect = givenAnswer == correctAnswer
            val currentEmotion = furhat.users.current.currentEmotion.emotion
            print("\n Current user emotion: $currentEmotion")

            if (isAnswerCorrect) {
                furhat.gazing(ConvMode.INTIMACY)
                emotionHandler.performGesture(furhat, "Happy")
                if (currentEmotion.equals("Happy") && users.current.currentEmotion.polarity >= 0.0f) {
                    furhat.say("I see that you are confident in your answer. And you should be!")
                } else {
                    furhat.say("Nice job!")
                }
                emotionHandler.performGesture(furhat, "Confirm")
                furhat.say("Your answer: " + it.intent.givenanswer + ", is correct.")
                emotionHandler.performGesture(furhat, "Neutral")
            } else {
                furhat.gazing(ConvMode.INTIMACY)
                if (currentEmotion.equals("Sad") && users.current.currentEmotion.polarity <= 0.0f) {
                    emotionHandler.performGesture(furhat, "Encouraging")
                    furhat.say("I'm sorry, your answer: " + it.intent.givenanswer + ", wasn't correct. I see you are a bit disappointed.")
                    emotionHandler.performGesture(furhat, "Uplifting")
                    furhat.say("But don't worry! We can practice a bit more.")
                } else if (currentEmotion.equals("Frustrated") && users.current.currentEmotion.polarity <= 0.0f) {
                    emotionHandler.performGesture(furhat, "Calming")
                    furhat.say("" + it.intent.givenanswer + ", wasn't the right answer, unfortunately. You look a bit frustrated, but we are almost there!")
                    emotionHandler.performGesture(furhat, "Uplifting")
                    furhat.say("Let's practice a bit more.")
                } else {
                    furhat.say("Oops, your answer: " + it.intent.givenanswer + ", wasn't correct. ")
                }
                emotionHandler.performGesture(furhat, "Neutral")
                furhat.say("The correct answer was: $correctAnswer")
            }
        } else {
            furhat.gesture(Gestures.ExpressSad, async = true)
            furhat.say("I'm sorry, I must have misunderstood.")
            reentry()
        }
        goto(GiveTrainingMode)
    }

    this.onResponse<DontKnow> {
        val currentEmotion = furhat.users.current.currentEmotion.emotion
        print("\n Current user emotion: $currentEmotion")
        furhat.gazing(ConvMode.COGNITIVE)
        when (currentEmotion) {
            "Sad" -> {
                furhat.gazing(ConvMode.INTIMACY)
                emotionHandler.performGesture(furhat, "Encouraging")
                furhat.say("Don't look so sad. It's okay to not know the answer.")

            }
            "Frustrated" -> {
                furhat.gazing(ConvMode.INTIMACY)
                emotionHandler.performGesture(furhat, "Calming")
                furhat.say("I know it's frustrating, but it's okay to not know the answer.")
            }
            else -> {
                furhat.gazing(ConvMode.INTIMACY)
                emotionHandler.performGesture(furhat, "Encouraging")
                furhat.say("That's okay.")
            }
        }
        furhat.gazing(ConvMode.INTIMACY)
        emotionHandler.performGesture(furhat, "Uplifting")
        furhat.say("The correct answer is: $correctAnswer")
        emotionHandler.performGesture(furhat, "Neutral")
        furhat.say("Let me know if I need to go over some more explanations.")
        goto(GiveTrainingMode)
    }

    this.onResponse<ExitProgram> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
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
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say {
                    +"What is the answer to $randomFirstValue multiplied by $randomSecondValue? "
                    +delay(3000)
                    +"The answer is: $answerm"
                }
            }
            "division" -> {
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say {
                    +"What is the answer to $dividend divided by $divisor? "
                    +delay(3000)
                    +"The answer is: $answerd"
                }
            }
            "percentages" -> {
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say {
                    +"What is $number percent out of $out_of? "
                    +delay(3000)
                    +"The answer is: $answerp"
                }
            }
            "fractions" -> {
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say {
                    +"What is $firstfraction above $divisor plus $secondfraction above $divisor? "
                    +"Please give your answer as: number above $divisor. "
                    +delay(3000)
                    +"The answer is: $answerf."
                }
            }
            else -> {
                furhat.gazing(ConvMode.INTIMACY)
                furhat.say("I'm sorry, the topic isn't clear to me, can you repeat it?")
                goto(Subject)
            }
        }
        goto(GiveTrainingMode)
    }
    this.onResponse<ExitProgram> {
        emotionHandler.socket_sentiment_pub.send(emotionHandler.sentiment_pub_topic + " " + it.text)
        call(Leave)
        reentry()
    }
}

/**
 * This states handles the explanation of topics
 */
val Explanation: State = state(Interaction) {
    onEntry {
        emotionHandler.performGesture(furhat, "Uplifting")
        furhat.say("Alright, you want the topic explained, let's start!")
        //todo: get better explanations as these are too basic
        emotionHandler.performGesture(furhat, "Neutral")
        when (currentsubject.currentSubject) {
            "multiplication" -> {
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say("In multiplication we perform repeated addition.")
                furhat.say("We use the × symbol to mean multiply")
                furhat.say("For example, if we have 3 people and they all have 5 candies, in total we have 5 + 5 + 5 = 5 × 3 = 15 candies.")
            }
            "division" -> {
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say("Division is sharing a certain amount equally among a group.")
                furhat.say("For example: there are 15 candies, and 3 friends want to share them, how do they divide the candies?")
                furhat.say("They should get 5 each.")
                furhat.say("We use the / slash symbol to mean divide so 15 / 3 = 5")
            }
            "percentages" -> {
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say("A percentage means amount per 100")
                furhat.say("The symbol is %")
                furhat.say("For example: 25% means 25 per 100")
                furhat.say("We can take a look at another example: 10% means 10 out of every 100.")
                furhat.say("So if 10% of 500 people have candy, then 50 people have candy.")

            }
            "fractions" -> {
                furhat.gazing(ConvMode.COGNITIVE)
                furhat.say("A fraction is how many parts of a whole:")
                furhat.say("the top number (the numerator) says how many parts we have.")
                furhat.say("The bottom number (the denominator) says how much the whole is divided into.")
                furhat.say("For example: if we slice a pie in 5 pieces and you and I both have one piece, we do 1 over 5 + 1 over 5 = 2 over 5. We have two of the five pieces.")
            }
            else -> {
                furhat.gazing(ConvMode.INTIMACY)
                furhat.say("I'm sorry, the topic isn't clear to me, can you repeat it?")
                goto(Subject)
            }
        }
        goto(GiveTrainingMode)
    }
}
