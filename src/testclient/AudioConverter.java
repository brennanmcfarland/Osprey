package testclient;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import static java.lang.Math.*;

public class AudioConverter {

    //given an array of raw sound bytes, returns the unpacked samples
    public static float[] unpack(byte[] rawSoundBytes, AudioFormat audioFormat) {
        int bitsPerSample = audioFormat.getSampleSizeInBits();
        int bytesPerSample = bytesPerSample(bitsPerSample);
        boolean isBigEndian = audioFormat.isBigEndian();
        Encoding encoding = audioFormat.getEncoding();
        double fullScale = fullScale(bitsPerSample);
        int byteLength = rawSoundBytes.length;
        float[] audioSamples = new float[byteLength];

        int i=0;
        int s=0;
        while(i<byteLength) {
            long temp = unpackBits(rawSoundBytes, i, isBigEndian, bytesPerSample);
            float audioSample = 0;

            if(encoding == Encoding.PCM_SIGNED) {
                temp = extendSign(temp, bitsPerSample);
                audioSample = (float)(temp/fullScale);
            } else if(encoding == Encoding.PCM_UNSIGNED) {
                temp = signUnsigned(temp, bitsPerSample);
                audioSample = (float)(temp/fullScale);
            } else if(encoding == Encoding.PCM_FLOAT) {
                if(bitsPerSample == 32) {
                    audioSample = Float.intBitsToFloat((int)temp);
                } else if(bitsPerSample == 64) {
                    audioSample = (float)Double.longBitsToDouble(temp);
                }
            } else if(encoding == Encoding.ULAW) {
                audioSample = bitsToMulaw(temp);
            } else if(encoding == Encoding.ALAW) {
                audioSample = bitsToAlaw(temp);
            }

            audioSamples[s] = audioSample;
            i += bytesPerSample;
            s++;
        }

        return audioSamples;
    }

    private static int bytesPerSample(int bitsPerSample) {
        return (int)ceil(bitsPerSample/8.0);
    }

    private static double fullScale(int bitsPerSample) {
        return pow(2.0, bitsPerSample-1);
    }

    private static long unpackBits(byte[] rawSoundBytes, int i, boolean isBigEndian, int bytesPerSample) {
        switch (bytesPerSample) {
            case 1: return unpack8Bit(rawSoundBytes, i);
            case 2: return unpack16Bit(rawSoundBytes, i, isBigEndian);
            case 3: return unpack24Bit(rawSoundBytes, i, isBigEndian);
            default: return unpackAnyBit(rawSoundBytes, i, isBigEndian, bytesPerSample);
        }
    }

    private static long unpack8Bit(byte[] bytes, int i) {
        return bytes[i] & 0xffL;
    }

    private static long unpack16Bit(byte[] bytes, int i, boolean isBigEndian) {
        if(isBigEndian) {
            return(
                    ((bytes[i] & 0xffL) << 8L)
                    | (bytes[i+1] & 0xffL));
        } else {
            return(
                    (bytes[i] & 0xffL)
                    | ((bytes[i+1] & 0xffL) << 8L));
        }
    }

    private static long unpack24Bit(byte[] bytes, int i, boolean isBigEndian) {
        if (isBigEndian) {
            return (
                    ((bytes[i    ] & 0xffL) << 16L)
                            | ((bytes[i + 1] & 0xffL) <<  8L)
                            |  (bytes[i + 2] & 0xffL));
        } else {
            return (
                    (bytes[i    ] & 0xffL)
                            | ((bytes[i + 1] & 0xffL) <<  8L)
                            | ((bytes[i + 2] & 0xffL) << 16L));
        }
    }

    private static long unpackAnyBit(byte[] bytes, int i, boolean isBigEndian, int bytesPerSample) {
        long temp = 0L;
        if (isBigEndian) {
            for (int b = 0; b < bytesPerSample; b++) {
                temp |= (bytes[i + b] & 0xffL) << (
                        8L * (bytesPerSample - b - 1L));
            }
        } else {
            for (int b = 0; b < bytesPerSample; b++) {
                temp |= (bytes[i + b] & 0xffL) << (8L * b);
            }
        }
        return temp;
    }

    private static long extendSign(long temp, int bitsPerSample) {
        int extensionBits = 64-bitsPerSample;
        return (temp << extensionBits) >> extensionBits;
    }

    private static long signUnsigned(long temp, int bitsPerSample) {
        return temp-(long)fullScale(bitsPerSample);
    }

    //mu-law constant
    private static final double MU = 255.0;

    private static float bitsToMulaw(long temp) {
        temp ^= 0xffL;
        if ((temp & 0x80L) == 0x80L) {
            temp = -(temp ^ 0x80L);
        }

        float sample = (float) (temp / fullScale(8));

        return (float) (
                signum(sample)
                        *
                        (1.0 / MU)
                        *
                        (pow(1.0 + MU, abs(sample)) - 1.0)
        );
    }

    //A-law constant
    private static final double A = 87.7;
    //ln(A)
    private static final double LN_A = log(A);
    //minimum threshold for values, below which A-law exponent is 0
    private static final double EXP_0 = 1.0 / (1.0 + LN_A);

    private static float bitsToAlaw(long temp) {
        temp ^= 0x55L;
        if ((temp & 0x80L) == 0x80L) {
            temp = -(temp ^ 0x80L);
        }

        float sample = (float) (temp / fullScale(8));

        float sign = signum(sample);
        sample = abs(sample);

        if (sample < EXP_0) {
            sample = (float) (sample * ((1.0 + LN_A) / A));
        } else {
            sample = (float) (exp((sample * (1.0 + LN_A)) - 1.0) / A);
        }

        return sign * sample;
    }
}
