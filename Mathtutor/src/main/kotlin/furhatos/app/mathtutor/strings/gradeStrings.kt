package furhatos.app.mathtutor.strings

interface GradeStrings {
    val finishTest: String
}

class GradeStringsEnglish: GradeStrings {
    override val finishTest: String
        get() = "Your test on "
}