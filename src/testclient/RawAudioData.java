package testclient;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

/*
Handles raw audio data; gets a target data line and gets raw sound data from it
 */
public class RawAudioData {

    private TargetDataLine audioDataLine;
    private int numBytesRead;
    //5 is a magic number from the tutorial
    private byte[] audioDataBuffer;
    private ByteArrayOutputStream audioDataStream;
    private AudioFormat audioFormat;

    public int getNumBytesRead() { return numBytesRead; }

    public RawAudioData(AudioFormat audioFormat) throws LineUnavailableException {
        audioDataLine = AudioSystem.getTargetDataLine(audioFormat);
        audioDataLine.open(audioFormat);
        audioDataBuffer = new byte[audioDataLine.getBufferSize()/5]; //5 is a magic number from the Oracle tutorial
        numBytesRead = 0;
        audioDataStream = new ByteArrayOutputStream();
        this.audioFormat = audioFormat;
    }

    public void startCapture() {
        audioDataLine.start();
    }

    public void captureNextChunk() {
        numBytesRead = audioDataLine.read(audioDataBuffer, 0, audioDataBuffer.length);
        audioDataStream.write(audioDataBuffer, 0, numBytesRead);
    }

    //get a more accurate (still unnormalized) estimate from LPCM data
    //TODO: make LPCM stuff its own class in the future
    public float getLPCMAmplitude() {
        try {
            float[] LPCMSoundData = AudioConverter.unpack(audioDataBuffer, audioFormat);
            float maxAmplitude = 0.0f;
            short sampleStepSize = 1000; //TODO: remove magic numbers
            for(int i=0; i<LPCMSoundData.length; i++) {
                if(i%sampleStepSize == 0 && LPCMSoundData[i] > maxAmplitude) {
                    maxAmplitude = LPCMSoundData[i];
                }
            }
            System.out.println(maxAmplitude);
            return maxAmplitude;
        } catch(Exception e) {
            //TODO: this is a bad/lossy conversion
            return (float)getRawAmplitude();
        }
    }

    //get a rough, unnormalized estimate of the amplitude from the raw sound data
    public long getRawAmplitude() {
        if(audioDataLine.isControlSupported(FloatControl.Type.VOLUME)) {
            System.out.println("supports volume!");
        }
        else {
            System.out.println("does not support volume :(");
        }
        long rawUnscaledAmplitude = 0;
        for(byte rawAudioByte : audioDataBuffer) {
            rawUnscaledAmplitude += rawAudioByte;
        }
        System.out.println(rawUnscaledAmplitude); //TODO: remove this
        return Math.abs(rawUnscaledAmplitude);
    }
}
