package furhatos.app.mathtutor.gaze
import furhatos.flow.kotlin.Furhat
import furhatos.records.Location
import org.apache.commons.math3.distribution.*
import org.apache.commons.math3.util.Pair

enum class ConvMode{
    INTIMACY,
    TURNTAKING,
    COGNITIVE
}

val cognitiveDistribution = EnumeratedDistribution<Location>(listOf(
    Pair(Location.UP, 0.393),
    Pair(Location.DOWN, 0.294),
    Pair(Location.LEFT, 0.313 / 2),
    Pair(Location.RIGHT, 0.313 / 2)
))

val intimacyDistribution = EnumeratedDistribution<Location>(listOf(
    Pair(Location.UP, 0.137), // UP
    Pair(Location.DOWN, 0.288), // DOWN
    Pair(Location.LEFT, 0.575 / 2), // LEFT
    Pair(Location.RIGHT, 0.575 / 2) // RIGHT
))

val turnTakingDistribution = EnumeratedDistribution<Location>(listOf(
    Pair(Location.UP, 0.213),
    Pair(Location.DOWN, 0.295),
    Pair(Location.LEFT, 0.492 / 2),
    Pair(Location.RIGHT, 0.492 / 2)
))


fun getGazeDirection(convFunction: ConvMode): Location {
    // Source: http://pages.cs.wisc.edu/~bilge/pubs/2013/IVA13-Andrist.pdf (Andrist, Mutlu and Gleicher 2013)

    return when(convFunction){
        ConvMode.COGNITIVE ->  cognitiveDistribution.sample()
        ConvMode.INTIMACY -> intimacyDistribution.sample()
        ConvMode.TURNTAKING -> turnTakingDistribution.sample()
    }
}

val intimacySpeakingDurationDistribution = NormalDistribution(1.96, 0.32)
val intimacyListeningDurationDistribution = NormalDistribution(1.14,0.27)
val cognitiveDurationDistribution = NormalDistribution(3.54, 1.26)
val turnTakingDurationDistribution = NormalDistribution(2.30, 1.10)

fun Furhat.getGazeDuration(convFunction: ConvMode) : Int{
    // Source: http://pages.cs.wisc.edu/~bilge/pubs/2013/IVA13-Andrist.pdf (Andrist, Mutlu and Gleicher 2013)

    fun intimacyGaze(): Double{
        if(isSpeaking){
            return intimacySpeakingDurationDistribution.sample()
        }
        return intimacyListeningDurationDistribution.sample()
    }

    return (when(convFunction) {
        ConvMode.COGNITIVE ->  cognitiveDurationDistribution.sample()
        ConvMode.INTIMACY -> intimacyGaze()
        ConvMode.TURNTAKING -> turnTakingDurationDistribution.sample()
    } * 1000).toInt()
}

fun Furhat.gazing(convFunction: ConvMode){
    glance(getGazeDirection(convFunction), getGazeDuration(convFunction), async = true)
}