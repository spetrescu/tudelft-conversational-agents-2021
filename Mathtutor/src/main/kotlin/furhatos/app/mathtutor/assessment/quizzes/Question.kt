package furhatos.app.mathtutor.assessment.quizzes

import furhatos.app.mathtutor.flow.Answer
import furhatos.app.mathtutor.strings.getTestStrings
import furhatos.util.Language

interface Question {
    val operandOne: Int
    val operandTwo: Int
    val operandThree: Int
    val language: Language
    fun getCorrectAnswer(): Int
}

abstract class AbstractQuestion: Question {
    var userAnswer: Answer? = null
}



class MultiplicationQuestion(
    override val operandOne: Int,
    override val operandTwo: Int,
    override val operandThree: Int,
    override val language: Language
) : AbstractQuestion() {

    override fun getCorrectAnswer(): Int {
        return operandOne * operandTwo
    }

    override fun toString(): String {
        return "$operandOne ${getTestStrings(language).multiplication} $operandTwo"
    }
}

class DivisionQuestion(override val operandOne: Int, override val operandTwo: Int, override val operandThree: Int, override val language: Language) :
    AbstractQuestion() {

    override fun getCorrectAnswer(): Int {
        return operandOne / operandTwo // Integer division
    }

    override fun toString(): String {
        return "$operandOne ${getTestStrings(language).division} $operandTwo"
    }
}

class PercentagesQuestion(override val operandOne: Int, override val operandTwo: Int, override val operandThree: Int, override val language: Language) :
    AbstractQuestion() {

    override fun getCorrectAnswer(): Int {
        return operandOne / operandTwo * 100 // Integer division
    }

    override fun toString(): String {
        return "$operandOne ${getTestStrings(language).percentages} $operandTwo"
    }
}

class FractionsQuestion(override val operandOne: Int, override val operandTwo: Int, override val operandThree: Int, override val language: Language) :
    AbstractQuestion() {

    override fun getCorrectAnswer(): Int {
        return operandOne + operandTwo // Integer division
    }

    override fun toString(): String {
        return "$operandOne above $operandThree ${getTestStrings(language).percentages} $operandTwo above $operandThree"
    }
}