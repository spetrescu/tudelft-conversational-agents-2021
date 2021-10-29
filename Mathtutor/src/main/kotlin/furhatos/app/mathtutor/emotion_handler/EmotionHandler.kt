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

    val socket_nlu = context.socket(zmq.ZMQ.ZMQ_SUB)
    val nlu_topic = "nlu_text"

    fun nluClient(user: User) {
        GlobalScope.launch {
            socket_nlu.subscribe(nlu_topic)
            print("\n\n\nConnecting to new server to get mrssages from Flask..\n\n\n")
            socket_nlu.connect("tcp://*:5568")
            print("Do something")
            while (true) {
                var message = socket_nlu.recvStr()
                print(message)
                print("Do something")

            }
        }
    }

    fun serverNluNlg(user: User) {
        GlobalScope.launch {
            val context = ZMQ.context(1)
            val socket = context.socket(ZMQ.REP)
            println("\nStarting server for communication with Flask App...\n")
            socket.bind("tcp://*:5897")
            println("Accepting connections on port 5897...\n")
            while (true) {
                val rawRequest = socket.recv(0)
                val cleanRequest = String(rawRequest, 0, rawRequest.size - 1)
                println("Server: Request received : $cleanRequest")
                var plainReply = "World "
                var rawReply = plainReply.toByteArray()
                rawReply[rawReply.size - 1] = 0
                socket.send(plainReply)
            }
        }
    }

    fun sendClientNluNlg(user: User) {
        GlobalScope.launch {
            val context = ZMQ.context(1)
            val socket = context.socket(ZMQ.REQ)
            socket.connect("tcp://localhost:5599")
            var plainRequest = "Hello"
            var byteRequest = plainRequest.toByteArray()
            byteRequest[byteRequest.size - 1] = 0
            println("Client: sending request $plainRequest")
            socket.send(byteRequest, 0)
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
            else -> {
                furhat.voice.pitch = "default"
                furhat.voice.rate = 1.0
                furhat.voice.volume = "medium"
            }
        }
    }



}