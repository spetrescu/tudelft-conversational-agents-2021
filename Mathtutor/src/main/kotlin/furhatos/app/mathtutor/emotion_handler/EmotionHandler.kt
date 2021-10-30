package furhatos.app.mathtutor.emotion_handler
import furhatos.app.mathtutor.object_classes.currentEmotion
import furhatos.app.mathtutor.object_classes.currentResponse
import furhatos.flow.kotlin.Furhat
import furhatos.flow.kotlin.furhat
import furhatos.gestures.BasicParams.*
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.records.User
import furhatos.util.CommonUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.zeromq.ZMQ

val context: ZMQ.Context = ZMQ.context(1)

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

val happyGestures = listOf(persistentSmile)
val encouragingGestures = listOf(Gestures.GazeAway, Gestures.Thoughtful, Gestures.ExpressSad)
val calmingGestures = listOf(Gestures.Thoughtful, Gestures.ExpressSad, Gestures.BrowFrown)

class EmotionHandler{
    val socket_emotions = context.socket(zmq.ZMQ.ZMQ_SUB)
    val socket_sentiment_pub = context.socket(zmq.ZMQ.ZMQ_PUB)
    val emotions_topic = "emotions"
    val sentiment_pub_topic = "sentiment_toServer"

    // Adapted from: https://github.com/FurhatRobotics/camerafeed-demo
    fun startEmotionHandler(user: User) {
        GlobalScope.launch {
            socket_emotions.subscribe(emotions_topic)
            print("\n\n\nConnecting to server..\n\n\n")
            socket_emotions.connect("tcp://*:5556")
            socket_sentiment_pub.connect("tcp://*:5557")
            while (true) {
                var message = socket_emotions.recvStr()
                print(message)
                var label = message.split(" ")[1]
                var polarity = message.split(" ")[2]
                user.currentEmotion.emotion = label
                user.currentEmotion.polarity = polarity.toFloat()
            }
        }
    }

    fun serverNluNlg(furhat: Furhat, user: User) {
        GlobalScope.launch {
            val context = ZMQ.context(1)
            val socket = context.socket(ZMQ.REP)
            println("\nStarting server for communication with Flask App...\n")
            socket.bind("tcp://*:5897")
            println("Accepting connections on port 5897...\n")
            while (true) {
                //val rawRequest = socket.recv(0)
                var rawRequestStr = socket.recvStr()
                //val cleanRequest = String(rawRequest, 0, rawRequest.size - 1)

                //println("serverNluNlg: Request received : $rawRequestStr")
                furhat.users.current.currentResponse.response = rawRequestStr.toString()
                //var plainReply = "World "
                //var rawReply = plainReply.toByteArray()
                //rawReply[rawReply.size - 1] = 0
                //socket.send(plainReply)
            }
        }
    }

    fun clientNluNlg(message: String) {
        GlobalScope.launch {
            val context = ZMQ.context(1)
            val socket = context.socket(ZMQ.REQ)
            //print("\nclientNluNlg: Send message - $message - to ZMQ REP\n")
            print("user: $message\n")
            socket.connect("tcp://localhost:5599")
            var plainRequest = message
            var byteRequest = plainRequest.toByteArray()
            byteRequest[byteRequest.size - 1] = 0
            //println("\nclientNluNlg: sending request $plainRequest")
            //socket.send(byteRequest, 0)
            socket.send(plainRequest)
        }
    }

    // acts with a gesture - pitch - rate - volume
    fun performGesture(furhat: Furhat, gesture: String) {
        print("\nActing " + gesture + "\n")
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
            else -> {
                furhat.voice.pitch = "default"
                furhat.voice.rate = 1.0
                furhat.voice.volume = "medium"
            }
        }
    }



}