package testclient;

public class AmplitudeThreshold implements ListenCondition {

    private final float threshold;

    public AmplitudeThreshold(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean check(RawAudioData audioData) {
        return(audioData.getLPCMAmplitude() > threshold);
    }
}
