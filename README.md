# MATHew

[Project Report](https://github.com/spetrescu/tudelft-conversational-agents-2021/blob/main/Report.pdf)

How to use the emotions server:

- Create two directories: `emotions_server/Emotion_detection/src/data/train` and `emotions_server/Emotion_detection/src/data/test`
- Open the contents of `emotions_server`, preferably as a project in PyCharm

Alter the run config:
- Script path: `emotions.py`
- Parameters: `--mode display`
- Python interpreter 3.6
- Working directory: `\emotions_server\Emotion-detection\src`

- (Make a venv and) install the packages in `requirements.txt` through pip

Now you can run the emotions server from PyCharm. A webcam feed should show up, and it should recognize your face and put a square around it, along with a text detecting the emotion (Happy, Neutral, Sad, or Frustrated) 

Turn on furhat and run the skill. In a few states (for example, getting a test result or furhat telling you if your answer is right or wrong) furhat will look at user emotion and respond accordingly:
Furhat responds:
- Happy when user is happy
- Neutral or encouraging when user is neutral
- Encouraging, then uplifting when user is sad
- Calming, then uplifting when user is frustrated

Each furhat 'emotional response' has a set of gestures it can pick from. It also alters the voice's pitch, rate and volume, similar to the table we saw in the lecture on Affects. 

## Authors
- Doreen Mulder
- Laura Ottevanger
- Stefan Petrescu
- Rohan Sobha
