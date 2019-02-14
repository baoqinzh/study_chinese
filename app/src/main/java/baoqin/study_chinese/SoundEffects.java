package baoqin.study_chinese;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Baoqinzh on 12/31/2017.
 */

public enum  SoundEffects {


    INSTANCE;

    Context mContext;

    public SoundPool mSoundPool,rightSoundPool,wrongSoundPool,completeSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap,rightSoundPoolMap,wrongSoundPoolMap,completeSoundPoolMap;
    private AudioManager mAudioManager;
    private float streamVolume;

    MediaPlayer player;
    boolean play_music = true; // true when the music should be played, false otherwise

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setContext(Context context) {

        mContext = context;
        mSoundPoolMap = new HashMap<Integer, Integer>();
        rightSoundPoolMap = new HashMap<Integer, Integer>();
        wrongSoundPoolMap = new HashMap<Integer, Integer>();
        completeSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        streamVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / (float) mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        player = MediaPlayer.create(mContext, R.raw.backgrondmusic);
        player.setLooping(true);
        player.setVolume(streamVolume/10,streamVolume/10);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
            rightSoundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
            wrongSoundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
            completeSoundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 1);
            rightSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 1);
            wrongSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 1);
            completeSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 1);
        }
        rightSoundPoolMap.put(0, rightSoundPool.load(context, R.raw.zhenbang,1));
        rightSoundPoolMap.put(1, rightSoundPool.load(context, R.raw.daduile,1));
        rightSoundPoolMap.put(2, rightSoundPool.load(context, R.raw.zhenlihai,1));
        rightSoundPoolMap.put(3, rightSoundPool.load(context, R.raw.bangjile,1));
        rightSoundPoolMap.put(4, rightSoundPool.load(context, R.raw.taibangle,1));

        wrongSoundPoolMap.put(0, wrongSoundPool.load(context, R.raw.dacuole,1));
        wrongSoundPoolMap.put(1, wrongSoundPool.load(context, R.raw.zaishishi,1));
        wrongSoundPoolMap.put(2, wrongSoundPool.load(context, R.raw.zailaiyici,1));
        wrongSoundPoolMap.put(3, wrongSoundPool.load(context, R.raw.jiayou,1));

        completeSoundPoolMap.put(0, completeSoundPool.load(context, R.raw.taibangle,1));

    }

    public void loadSound(Context context,int soundId,int ResousId){
        mSoundPoolMap.put(soundId, mSoundPool.load(context, ResousId,1));
    }

    public int getNumSounds() {
        return mSoundPoolMap.size();
    }
    public int getRightNumSounds() {
        return rightSoundPoolMap.size(); }
    public int getWrongNumSounds() {
        return wrongSoundPoolMap.size();
    }

    public void playSound(int sound) {
        if (mSoundPoolMap.get(sound) == null){
        }
        mSoundPool.play(mSoundPoolMap.get(sound), streamVolume, streamVolume, 100, 0, 1f);
    }
    public void playRightSound(int sound) {
        rightSoundPool.play(rightSoundPoolMap.get(sound), streamVolume, streamVolume, 100, 0, 1f);
    }
    public void playWrongSound(int sound) {
        wrongSoundPool.play(wrongSoundPoolMap.get(sound), streamVolume, streamVolume, 100, 0, 1f);
    }
    public void playCompleteSound() {
        completeSoundPool.play(completeSoundPoolMap.get(0), streamVolume, streamVolume, 100, 0, 1f);
    }
}

