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
import java.util.Collections;
import java.util.List;

/**
 * Created by Baoqin on 1/6/2018.
 */

public class PracticeCharacters extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "PracticeCharacters";
    private TextView displayName1,displayName2,displayName3,displayName4,displayName5;
    private ImageButton imgButtonSound;
    private String groupName,displayName;
    private String groupNum = "Gp1";
    private String itemInfoString;
    private int wrongNum = 0 , wrongMaxNum = 4;
    private int rightNum = 0 , rightMaxNum = 5;
    private ArrayList <Integer> displayList = new ArrayList();

    Menu menu;

    int displayNum = 0;
    int displayMax = 5;

    Context mContext;
    List<GroupActivity.ItemInfo> itemInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_characters);

        displayName1 = (TextView) findViewById(R.id.displayName1);
        displayName2 = (TextView) findViewById(R.id.displayName2);
        displayName3 = (TextView) findViewById(R.id.displayName3);
        displayName4 = (TextView) findViewById(R.id.displayName4);
        displayName5 = (TextView) findViewById(R.id.displayName5);
        imgButtonSound = (ImageButton) findViewById(R.id.imgButtonSound);

        displayName1.setOnClickListener(this);
        displayName2.setOnClickListener(this);
        displayName3.setOnClickListener(this);
        displayName4.setOnClickListener(this);
        displayName5.setOnClickListener(this);
        imgButtonSound.setOnClickListener(this);

        for(int i=0; i<displayMax; i++){
            displayList.add(i);
        }

        // get the title from the Intent
        Intent in = getIntent();
        groupName = in.getStringExtra(getString(R.string.groupName));
        groupNum = in.getStringExtra(getString(R.string.groupNum));
        getSupportActionBar().setTitle(groupName);
        try{
            itemInfoString = in.getStringExtra("itemInfoString");
            processItemInfo(itemInfoString);
            if(itemInfo != null){
                SoundEffects.INSTANCE.playSound(displayNum);
                displayName = itemInfo.get(displayNum).displayName;
                displayName1.setText(itemInfo.get(0).displayName);
                displayName2.setText(itemInfo.get(1).displayName);
                displayName3.setText(itemInfo.get(2).displayName);
                displayName4.setText(itemInfo.get(3).displayName);
                displayName5.setText(itemInfo.get(4).displayName);
            }else {
                //Log.i(TAG,"itemInfo is null!");
            }
        }catch (Exception e){
            //Log.i(TAG,e.getLocalizedMessage());
        }
    }

    @Override
    public void onClick(View v) {
        // Using the View's ID to distinguish which button was clicked
        switch(v.getId()) {

            case R.id.displayName1:
                if(displayName == displayName1.getText()){
                    onClickRight();
                }else {
                    onClickWrong();
                }
                break;
            case R.id.displayName2:
                if(displayName == displayName2.getText()){
                    onClickRight();
                }else {
                    onClickWrong();
                }
                break;
            case R.id.displayName3:
                if(displayName == displayName3.getText()){
                    onClickRight();
                }else{
                    onClickWrong();
                }
                break;
            case R.id.displayName4:
                if(displayName == displayName4.getText()){
                    onClickRight();
                }else{
                    onClickWrong();
                }
                break;
            case R.id.displayName5:
                if(displayName == displayName5.getText()){
                    onClickRight();
                }else {
                    onClickWrong();
                }
                break;
            case R.id.imgButtonSound:
                SoundEffects.INSTANCE.playSound(displayNum);
                break;
            default:
                break;
        }
    }
    public void onClickRight(){
        if(displayNum == displayMax-1){
            SoundEffects.INSTANCE.playCompleteSound();
            returnToGroupActivity();
        }else {
            if(rightNum < rightMaxNum){
                SoundEffects.INSTANCE.playRightSound(rightNum++);
            }else{
                rightNum = 0;
                SoundEffects.INSTANCE.playRightSound(rightNum++);
            }
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                //Log.i(TAG,"SoundEffects did not wait!!"+e.getLocalizedMessage());
            }
            setDisplayNamesRandom();
            changeDisplayName();
        }
    }

    private void returnToGroupActivity() {
        Intent mIntent = new Intent(this,GroupActivity.class);
        mIntent.putExtra(getString(R.string.groupName),groupName);
        mIntent.putExtra(getString(R.string.groupNum),groupNum);
        startActivity(mIntent);
    }

    public void onClickWrong(){
        if(wrongNum < wrongMaxNum){
            SoundEffects.INSTANCE.playWrongSound(wrongNum++);
        }else{
            wrongNum = 0;
            SoundEffects.INSTANCE.playWrongSound(wrongNum++);
        }
        setDisplayNamesRandom();
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
            returnToGroupActivity();
            return true;
        }else if (id == R.id.action_sound) {
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
    public int randomWithRange()
    {
        int max = displayMax-1;
        int min = 0;
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    public void changeDisplayName(){
        displayNum++;
        SoundEffects.INSTANCE.playSound(displayNum);
        displayName = itemInfo.get(displayNum).displayName;
    }
    public void setDisplayNamesRandom(){
        Collections.shuffle(displayList);
        displayName1.setText(itemInfo.get(displayList.get(0)).displayName);
        displayName2.setText(itemInfo.get(displayList.get(1)).displayName);
        displayName3.setText(itemInfo.get(displayList.get(2)).displayName);
        displayName4.setText(itemInfo.get(displayList.get(3)).displayName);
        displayName5.setText(itemInfo.get(displayList.get(4)).displayName);
    }
}

