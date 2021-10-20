package furhatos.app.mathtutor.emotion_handler
import furhatos.app.mathtutor.object_classes.currentEmotion
import furhatos.flow.kotlin.*
import furhatos.records.User
import furhatos.util.CommonUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.zeromq.ZMQ

val logger = CommonUtils.getRootLogger()
val context: ZMQ.Context = ZMQ.context(1)
val socket = context.socket(zmq.ZMQ.ZMQ_SUB)

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
                // logger.warn(label)
            }
        }
    }

    fun getUserEmotion(user: User){
        var emotion = user.currentEmotion
        logger.warn("Current user emotion: " + emotion.emotion)
    }

    fun getHappyBehavior(){
        return
    }

    fun getSympathyBehavior(){
        return
    }

    fun getCalmingBehavior(){
        return
    }

    fun getNeutralBehavior(){
        return
    }


}