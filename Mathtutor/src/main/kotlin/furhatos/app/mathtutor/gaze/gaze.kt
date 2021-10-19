package furhatos.app.mathtutor.gaze
import furhatos.flow.kotlin.Furhat
import org.apache.commons.math3.distribution.NormalDistribution

fun Furhat.getGaze(convFunction: String) : Double{
    // Source: http://pages.cs.wisc.edu/~bilge/pubs/2013/IVA13-Andrist.pdf (Andrist, Mutlu and Gleicher 2013)

    fun intimacyGaze(): Double {

        val nd = NormalDistribution(3.54, 1.26)
        return nd.sample()
    }
    fun turnTakingGaze(): Double{
        if(isSpeaking){
            return NormalDistribution(1.96, 0.32).sample()
        }
        // isListening
        return NormalDistribution(1.14,0.27).sample()
    }
    return when(convFunction) {
        "cognitive" ->  NormalDistribution(3.54, 1.26).sample()
        "intimacy" -> intimacyGaze()
        "turn-taking" -> NormalDistribution(2.30, 1.10).sample()
        else -> intimacyGaze()
    }
}