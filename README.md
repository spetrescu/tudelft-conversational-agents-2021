# tudelft-conversational-agents-2021

# Code style
Please adhere to them as much as possible.
The app is located under `/src/main/kotlin/furhatos.app.mathtutor`
Every state in the dialog state is represented by a file in the `flow` folder with the corresponding state in Kotlin.

The intents are written as classes inside the `nlu` folder.

On startup, `main.kt` runs and brings the agent into the `Idle` state which is defined in `general.kt`.

Preferably write all code in Kotlin to ensure interoperability under `/src/main/Kotlin`.

All code related to Furhat should be put under `/src/main/kotlin/furhatos.app.mathtutor`.

If for some reason, Python (or another language) would suit better, please put the code under `src/main/<other language>/<other folders>`

If your code is dependent on someone else's, please put comments indicating where one should enter/leave the state.

**All states should inherit the Interaction state to ensure the fallback state works!**


