package furhatos.app.mathtutor.strings

import furhatos.util.Language
import org.junit.Test
import kotlin.reflect.typeOf


internal class FallbackStringsTest {

    @Test
    fun getFallBackStateStringsTestDutch(){
        assert(getFallBackStateStrings(Language.DUTCH) is FallbackStringsDutch)
    }

    @Test
    fun getFallBackStateStringsTestEnglish(){
        assert(getFallBackStateStrings(Language.ENGLISH_US) is FallbackStringsEnglish)
    }

    @Test
    fun getFallBackStateStringsTestElse(){
        assert(getFallBackStateStrings(Language.AFRIKAANS) is FallbackStringsEnglish)
    }
}