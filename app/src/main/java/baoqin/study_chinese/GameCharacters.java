package baoqin.study_chinese;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Baoqinzh on 1/6/2018.
 */

public class GameCharacters extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "GameCharacters";
    private TextView firstClickName,displayName1,displayName2,displayName3,displayName4,displayName5,
            displayName6,displayName7,displayName8,displayName9,displayName10,displayName11,displayName12;
    private String groupName;
    private String groupNum = "Gp1";
    private String itemInfoString;
    private int matchNum = 0;
    private ArrayList displayList = new ArrayList();
    private Drawable bgString;
    final Handler handler = new Handler();

    Menu menu;
    int displayMax = 5;

    Context mContext;
    List<GroupActivity.ItemInfo> itemInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_characters);

        displayName1 = (TextView) findViewById(R.id.displayName1);
        displayName2 = (TextView) findViewById(R.id.displayName2);
        displayName3 = (TextView) findViewById(R.id.displayName3);
        displayName4 = (TextView) findViewById(R.id.displayName4);
        displayName5 = (TextView) findViewById(R.id.displayName5);
        displayName6 = (TextView) findViewById(R.id.displayName6);
        displayName7 = (TextView) findViewById(R.id.displayName7);
        displayName8 = (TextView) findViewById(R.id.displayName8);
        displayName9 = (TextView) findViewById(R.id.displayName9);
        displayName10 = (TextView) findViewById(R.id.displayName10);
        displayName11 = (TextView) findViewById(R.id.displayName11);
        displayName12 = (TextView) findViewById(R.id.displayName12);

        displayName1.setOnClickListener(this);
        displayName2.setOnClickListener(this);
        displayName3.setOnClickListener(this);
        displayName4.setOnClickListener(this);
        displayName5.setOnClickListener(this);
        displayName6.setOnClickListener(this);
        displayName7.setOnClickListener(this);
        displayName8.setOnClickListener(this);
        displayName9.setOnClickListener(this);
        displayName10.setOnClickListener(this);
        displayName11.setOnClickListener(this);
        displayName12.setOnClickListener(this);

        bgString = displayName1.getBackground();
        // get the title from the Intent
        Intent in = getIntent();
        groupName = in.getStringExtra(getString(R.string.groupName));
        groupNum = in.getStringExtra(getString(R.string.groupNum));
        getSupportActionBar().setTitle(groupName);
        try{
            itemInfoString = in.getStringExtra("itemInfoString");
            processItemInfo(itemInfoString);
            displayList.add(itemInfo.get(0).displayName);
            displayList.add(itemInfo.get(0).displayName);
            for(int i=0; i<itemInfo.size();i++){
                displayList.add(itemInfo.get(i).displayName);
                displayList.add(itemInfo.get(i).displayName);
            }
            Collections.shuffle(displayList);
        }catch (Exception e){
            //Log.i(TAG,e.getLocalizedMessage());
        }
    }

    @Override
    public void onClick(View v) {
        // Using the View's ID to distinguish which button was clicked
        //Log.i(TAG,"itemInfo.size = " + itemInfo.size() + "displayNum = " + displayNum +",itemInfo.displayName = " + itemInfo.get(displayNum).displayName);
        switch(v.getId()) {

            case R.id.displayName1:
                displayName1.setText(displayList.get(0).toString());
                onClickDisplay(displayName1);
                break;
            case R.id.displayName2:
                displayName2.setText(displayList.get(1).toString());
                onClickDisplay(displayName2);
                break;
            case R.id.displayName3:
                displayName3.setText(displayList.get(2).toString());
                onClickDisplay(displayName3);
                break;
            case R.id.displayName4:
                displayName4.setText(displayList.get(3).toString());
                onClickDisplay(displayName4);
                break;
            case R.id.displayName5:
                displayName5.setText(displayList.get(4).toString());
                onClickDisplay(displayName5);
                break;
            case R.id.displayName6:
                displayName6.setText(displayList.get(5).toString());
                onClickDisplay(displayName6);
                break;
            case R.id.displayName7:
                displayName7.setText(displayList.get(6).toString());
                onClickDisplay(displayName7);
                break;
            case R.id.displayName8:
                displayName8.setText(displayList.get(7).toString());
                onClickDisplay(displayName8);
                break;
            case R.id.displayName9:
                displayName9.setText(displayList.get(8).toString());
                onClickDisplay(displayName9);
                break;
            case R.id.displayName10:
                displayName10.setText(displayList.get(9).toString());
                onClickDisplay(displayName10);
                break;
            case R.id.displayName11:
                displayName11.setText(displayList.get(10).toString());
                onClickDisplay(displayName11);
                break;
            case R.id.displayName12:
                displayName12.setText(displayList.get(11).toString());
                onClickDisplay(displayName12);
                break;
            default:
                break;
        }
    }
    private void onClickDisplay(TextView displayName) {
        displayName.setBackground(null);
        if(firstClickName==null){
            firstClickName = displayName;
        }else {
            //onClick the same one
            if(firstClickName == displayName){
                return;
            }
            try{

                // Create an animation instance
                Animation an = new RotateAnimation(-3, 3, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                // Set the animation's parameters
                an.setDuration(500);               // duration in ms
                an.setRepeatCount(1);                // -1 = infinite repeated
                an.setRepeatMode(Animation.REVERSE);


                if(firstClickName.getText().toString() == displayName.getText().toString()){
                    //Log.i(TAG,"you make a match, good job!!!");

                    firstClickName.setAnimation(an);
                    displayName.setAnimation(an);
                    firstClickName.setVisibility(View.INVISIBLE);
                    displayName.setVisibility(View.INVISIBLE);
                    matchNum++;
                    if(matchNum ==displayMax+1){
                        SoundEffects.INSTANCE.playCompleteSound();
                        returnToGroupActivity();
                    }
                }else {
                    //Log.i(TAG,"you'd better be luck next time!");
                    firstClickName.setAnimation(an);
                    displayName.setAnimation(an);

                    firstClickName.setText("");
                    displayName.setText("");
                    firstClickName.setBackground(bgString);
                    displayName.setBackground(bgString);

                }
            }catch (Exception e){
                //Log.i(TAG,e.getLocalizedMessage());
            }

            firstClickName = null;
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
    private void returnToGroupActivity() {
        Intent mIntent = new Intent(this,GroupActivity.class);
        mIntent.putExtra(getString(R.string.groupName),groupName);
        mIntent.putExtra(getString(R.string.groupNum),groupNum);
        startActivity(mIntent);
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
}

