package testclient;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class Listener {

    private ListenCondition listenCondition;

    public Listener(ListenCondition listenCondition) {
        this.listenCondition = listenCondition;
    }

    //print a list of the available mixers and their associated info
    public void printMixersInfo() {
        Mixer.Info[] systemMixerInfos = AudioSystem.getMixerInfo();
        for(Mixer.Info systemMixerInfo : systemMixerInfos) {
            System.out.println(systemMixerInfo.getDescription());
            System.out.println(systemMixerInfo.toString());
        }
    }

    //continually run until the condition is triggered, then return it
    public ListenCondition listen() {
        try {
            //get the default audio format for the current OS
            AudioFormat defaultAudioFormat = new AudioFormat(8000.0f, 16, 1, true, true);
            RawAudioData rawMicrophoneData = new RawAudioData(defaultAudioFormat);
            rawMicrophoneData.startCapture();
            while(true) {
                rawMicrophoneData.captureNextChunk();
                if(listenCondition.check(rawMicrophoneData)) {
                    return listenCondition;
                }
            }
        } catch(LineUnavailableException e) {
            System.out.println("Error: line unavailable");
        }
        return null;
    }
}
