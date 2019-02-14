package baoqin.study_chinese;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Baoqinzh on 1/6/2018.
 */

public class StudyCharacters  extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "StudyCharacters";
    private ImageButton buttonForward,buttonBackward,buttonRotate;
    private TextView displayName;
    private String groupName;
    private String groupNum = "Gp1";
    private String itemInfoString;

    Menu menu;

    int displayNum = 0;
    int displayMax = 5;

    Context mContext;
    List<GroupActivity.ItemInfo> itemInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_characters);

        buttonBackward = (ImageButton) findViewById(R.id.buttonBackward);
        buttonForward = (ImageButton) findViewById(R.id.buttonForward);
        buttonRotate = (ImageButton) findViewById(R.id.buttonRotate) ;
        displayName = (TextView) findViewById(R.id.displayName);

        buttonBackward.setOnClickListener(this);
        buttonForward.setOnClickListener(this);
        displayName.setOnClickListener(this);
        buttonRotate.setOnClickListener(this);

        // get the title from the Intent
        Intent in = getIntent();
        groupName = in.getStringExtra(getString(R.string.groupName));
        groupNum = in.getStringExtra(getString(R.string.groupNum));
        getSupportActionBar().setTitle(groupName);
        buttonBackward.setVisibility(View.INVISIBLE);
        displayName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        try{
            itemInfoString = in.getStringExtra("itemInfoString");
            processItemInfo(itemInfoString);
            if(itemInfo != null){
                buttonForward.setVisibility(View.VISIBLE);
                displayName.setText(itemInfo.get(displayNum).displayName);
                SoundEffects.INSTANCE.playSound(displayNum);
            }else {
                //Log.i(TAG,"itemInfo is null!");
                buttonForward.setVisibility(View.INVISIBLE);
                buttonRotate.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            //Log.i(TAG,e.getLocalizedMessage());
        }
    }

    @Override
    public void onClick(View v) {
        // Using the View's ID to distinguish which button was clicked
        //Log.i(TAG,"itemInfo.size = " + itemInfo.size() + "displayNum = " + displayNum +",itemInfo.displayName = " + itemInfo.get(displayNum).displayName);
        switch(v.getId()) {

            case R.id.buttonBackward:
                buttonForward.setVisibility(View.VISIBLE);
                if (displayNum <= 0){
                    buttonBackward.setVisibility(View.INVISIBLE);
                }else {
                    displayNum--;
                    displayName.setText(itemInfo.get(displayNum).displayName);
                    SoundEffects.INSTANCE.playSound(displayNum);
                    if(displayNum == 0){
                        buttonBackward.setVisibility(View.INVISIBLE);
                    }
                }
                break;

            case R.id.buttonForward:
                buttonBackward.setVisibility(View.VISIBLE);
                if(displayNum >= displayMax){
                    buttonForward.setVisibility(View.INVISIBLE);
                }else{
                    displayNum++;
                    displayName.setText(itemInfo.get(displayNum).displayName);
                    SoundEffects.INSTANCE.playSound(displayNum);
                    if (displayNum == displayMax - 1){
                        buttonForward.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.displayName:
                SoundEffects.INSTANCE.playSound(displayNum);
                break;
            case R.id.buttonRotate:
                displayNum = 0;
                displayName.setText(itemInfo.get(displayNum).displayName);
                SoundEffects.INSTANCE.playSound(displayNum);
                buttonBackward.setVisibility(View.INVISIBLE);
                buttonForward.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_study_characters, menu);

        this.menu = menu;
        if (SoundEffects.INSTANCE.play_music) {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
        }
        else {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_go_back) {
            Intent mIntent = new Intent(this,GroupActivity.class);
            mIntent.putExtra(getString(R.string.groupName),groupName);
            mIntent.putExtra(getString(R.string.groupNum),groupNum);
            startActivity(mIntent);
            return true;
        }
        else if (id == R.id.action_sound) {

            if (SoundEffects.INSTANCE.play_music) {
                SoundEffects.INSTANCE.player.pause();
                SoundEffects.INSTANCE.play_music=false;
                menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_up_white_24dp);

            }
            else {
                SoundEffects.INSTANCE.player.start();
                SoundEffects.INSTANCE.play_music=true;
                menu.findItem(R.id.action_sound).setIcon(R.drawable.ic_volume_off_white_24dp);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (SoundEffects.INSTANCE.play_music)
            SoundEffects.INSTANCE.player.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SoundEffects.INSTANCE.play_music)
            SoundEffects.INSTANCE.player.start();
    }

    @Override
    protected void onDestroy() {
/*
        if(SoundEffects.INSTANCE.player != null){
            SoundEffects.INSTANCE.player.stop();
            SoundEffects.INSTANCE.player.reset();
            SoundEffects.INSTANCE.player.release();
            SoundEffects.INSTANCE.player = null;
            SoundEffects.INSTANCE.play_music = false;
        }*/
        super.onDestroy();
    }
    // This class processes the Json string and converts it into a list of FriendInfo objects
    // We make use of the Gson library to do this automatically
    private void processItemInfo(String infoString) {

        // Create a new Gson object
        // TODO Create a Gson Object
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        // Use the Gson library to automatically process the string and convert it into
        // the list of FriendInfo objects. The use of the library saves you the need for
        // writing your own code to process the Json string
        // TODO convert the string to a list objects using Gson
        itemInfo = new ArrayList<GroupActivity.ItemInfo>();
        itemInfo = Arrays.asList(gson.fromJson(infoString, GroupActivity.ItemInfo[].class));
    }
}

