package baoqin.study_chinese;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Baoqinzh on 12/31/2017.
 */

public class GroupActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "GroupActivity";
    private TextView studyCharacter,practice,game;
    private String groupName,groupNum="Gp1";
    private String itemInfoString;
    Menu menu;
    Context mContext;

    public class ItemInfo  {
        int id;
        String displayName;
        String soundUrl;
    }

    List<ItemInfo> itemInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mContext = this;

        studyCharacter = (TextView) findViewById(R.id.studyCharacter);
        practice = (TextView) findViewById(R.id.practice);
        game = (TextView) findViewById(R.id.game);

        studyCharacter.setOnClickListener(this);
        practice.setOnClickListener(this);
        game.setOnClickListener(this);

        // get the title from the Intent
        Intent in = getIntent();
        groupName = in.getStringExtra(getString(R.string.groupName));
        groupNum = in.getStringExtra(getString(R.string.groupNum));
        getSupportActionBar().setTitle(groupName);

        // read db.json from assets
        String jsonString = loadJSONFromAsset(mContext);
        try {
            JSONObject json = new JSONObject(jsonString);
            itemInfoString = json.get(Constants.JSON_DB+groupNum).toString();
            processItemInfo(itemInfoString);
            loadSounds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent();
        switch(v.getId()) {
            case R.id.studyCharacter:
                mIntent = new Intent(this,StudyCharacters.class);
                break;
            case R.id.practice:
                mIntent = new Intent(this,PracticeCharacters.class);
                break;
            case R.id.game:
                mIntent = new Intent(this,GameCharacters.class);
                break;
            default:
                break;
        }
        mIntent.putExtra(getString(R.string.groupName), groupName);
        mIntent.putExtra(getString(R.string.groupNum),groupNum);

        if(itemInfoString!=null){
            mIntent.putExtra("itemInfoString", itemInfoString);
        }else {
            //Log.i(TAG,"itemInfoString is null!");
        }
        startActivity(mIntent);
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
            Intent mIntent = new Intent(this,MainActivity.class);
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
        super.onDestroy();
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("db.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

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
        itemInfo = new ArrayList<ItemInfo>();
        itemInfo = Arrays.asList(gson.fromJson(infoString, ItemInfo[].class));
    }
    private void loadSounds(){
        if(itemInfo.size()!=0){
            for(int i=0;i<itemInfo.size();i++){
                SoundEffects.INSTANCE.loadSound(this,i,this.getResources().getIdentifier(itemInfo.get(i).soundUrl, "raw", this.getPackageName()));
            }
        }
    }
}

