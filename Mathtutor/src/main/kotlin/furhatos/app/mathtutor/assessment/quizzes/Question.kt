package furhatos.app.mathtutor.assessment.quizzes

import furhatos.app.mathtutor.flow.Answer
import furhatos.app.mathtutor.strings.getTestStrings
import furhatos.util.Language

interface Question {
    val operandOne: Int
    val operandTwo: Int
    val language: Language
    fun getCorrectAnswer(): Int
}

abstract class AbstractQuestion: Question {
    var userAnswer: Answer? = null
}

class PercentageQuestion(override val operandOne: Int, override val operandTwo: Int, override val language: Language) :
    AbstractQuestion() {
    override fun getCorrectAnswer(): Int {
        return operandOne + operandTwo
    }

    override fun toString(): String {
        return "$operandOne ${getTestStrings(language).addition} $operandTwo"
    }
}

class FractionQuestion(override val operandOne: Int, override val operandTwo: Int, override val language: Language) :
    Question {
    override fun getCorrectAnswer(): Int {
        return operandOne - operandTwo
    }

    override fun toString(): String {
        return "$operandOne ${getTestStrings(language).subtraction} $operandTwo"
    }
}

class MultiplicationQuestion(
    override val operandOne: Int,
    override val operandTwo: Int,
    override val language: Language
) : AbstractQuestion() {

    override fun getCorrectAnswer(): Int {
        return operandOne * operandTwo
    }

    override fun toString(): String {
        return "$operandOne ${getTestStrings(language).multiplication} $operandTwo"
    }
}

class DivisionQuestion(override val operandOne: Int, override val operandTwo: Int, override val language: Language) :
    AbstractQuestion() {

    override fun getCorrectAnswer(): Int {
        return operandOne / operandTwo // Integer division
    }

    override fun toString(): String {
        return "$operandOne ${getTestStrings(language).division} $operandTwo"
    }
}