package furhatos.app.mathtutor.emotion_handler
import furhatos.app.mathtutor.object_classes.currentEmotion
import furhatos.flow.kotlin.Furhat
import furhatos.gestures.BasicParams.*
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.records.User
import furhatos.util.CommonUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.zeromq.ZMQ

val logger = CommonUtils.getRootLogger()
val context: ZMQ.Context = ZMQ.context(1)
val socket = context.socket(zmq.ZMQ.ZMQ_SUB)

val persistentSmile = defineGesture("PersistentSmile"){
    frame(0.0, 1.5) {
        SMILE_OPEN to 1.0
    }
    frame(1.5, 2.0){
        SMILE_OPEN to 0.0
        SMILE_CLOSED to 1.0
    }
    reset(4.0)
}

val responseDict = mapOf(
    "Happy" to "Happy",
    "Sad" to "Encouraging",
    "Frustrated" to "Calming",
    "Neutral" to "Neutral"
)

val happyGestures = listOf(persistentSmile)
val encouragingGestures = listOf(Gestures.GazeAway, Gestures.Thoughtful, Gestures.ExpressSad)
val calmingGestures = listOf(Gestures.Thoughtful, Gestures.ExpressSad, Gestures.BrowFrown)

class EmotionHandler{
    // Adapted from: https://github.com/FurhatRobotics/camerafeed-demo
    fun startEmotionHandler(user: User) {
        GlobalScope.launch {
            socket.subscribe("furhat")
            print("Connecting to server..")
            socket.connect("tcp://*:5556")
            while (true) {
                val message = socket.recvStr()
                var label = message.split(" ")[1]
                user.currentEmotion.emotion = label
                //logger.warn(label)
            }
        }
    }

    // acts with a gesture - pitch - rate - volume
    fun performGesture(furhat: Furhat, gesture: String) {
        print("\nActing " + gesture)
        when (gesture){
            "Happy" -> {
                furhat.gesture(happyGestures.random(), async = true)
                furhat.voice.pitch = "+15%"
                furhat.voice.rate = 1.0
                furhat.voice.volume = "+6dB"
            }
            "Confirm" -> {
                furhat.gesture(Gestures.Nod(duration = .5), async = true, priority = 10)
                furhat.gesture(Gestures.Smile(duration=2.0), async = true, priority = 10)
                furhat.voice.pitch = "+5%"
                furhat.voice.rate = 1.0
                furhat.voice.volume = "+3dB"
            }
            "Uplifting" -> {
                furhat.voice.pitch = "+5%"
                furhat.voice.rate = 1.0
                furhat.voice.volume = "medium"
            }
            "Encouraging" -> {
                furhat.gesture(encouragingGestures.random(), async = true)
                furhat.voice.pitch = "-5%"
                furhat.voice.rate = 1.0
                furhat.voice.volume = "-3dB"
            }
            "Calming" -> {
                furhat.gesture(calmingGestures.random(), async = true)
                furhat.voice.pitch = "-15%"
                furhat.voice.rate = 0.95
                furhat.voice.volume = "-6dB"
            }
            "Neutral" -> {
                furhat.voice.pitch = "default"
                furhat.voice.rate = 1.0
                furhat.voice.volume = "medium"
            }
            else -> print("help")
        }
    }



}