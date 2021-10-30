package furhatos.app.mathtutor.object_classes
import furhatos.nlu.common.Number
import furhatos.nlu.common.PersonName
import furhatos.records.User

class UserName(
    var name : PersonName? = null
)

class Emotion(
    var emotion : String = "Neutral",
    var polarity : Float = 0.0f
)

class Response(
    var response : String = "No response"
)

val User.userName : UserName
    get() = data.getOrPut(UserName::class.qualifiedName, UserName())

val User.currentEmotion : Emotion
    get() = data.getOrPut(Emotion::class.qualifiedName, Emotion())

val User.currentResponse : Response
    get() = data.getOrPut(Response::class.qualifiedName, Response())