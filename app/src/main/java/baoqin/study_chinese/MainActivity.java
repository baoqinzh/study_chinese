package baoqin.study_chinese;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private TextView group1,group2,group3,group4,group5;
    private String groupName;
    private String groupNum = "Gp1";

    Context mContext;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        group1 = (TextView) findViewById(R.id.group1);
        group2 = (TextView) findViewById(R.id.group2);
        group3 = (TextView) findViewById(R.id.group3);
        group4 = (TextView) findViewById(R.id.group4);
        group5 = (TextView) findViewById(R.id.group5);

        group1.setOnClickListener(this);
        group2.setOnClickListener(this);
        group3.setOnClickListener(this);
        group4.setOnClickListener(this);
        group5.setOnClickListener(this);

        // Set the context fo the SoundEffects singleton class
        // TODO Add context to the SoundEffects class
        SoundEffects.INSTANCE.setContext(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.group1:
                groupNum = "Gp1";
                groupName = "幼儿汉语1 第一组";
                break;
            case R.id.group2:
                groupNum = "Gp2";
                groupName = "幼儿汉语1 第二组";
                break;
            case R.id.group3:
                groupNum = "Gp3";
                groupName = "幼儿汉语1 第三组";
                break;
            case R.id.group4:
                groupNum = "Gp4";
                groupName = "幼儿汉语1 第四组";
                break;
            case R.id.group5:
                groupNum = "Gp5";
                groupName = "幼儿汉语1 第五组";
                break;
            default:
                break;
        }
        Intent mIntent = new Intent(this,GroupActivity.class);
        mIntent.putExtra(getString(R.string.groupName), groupName);
        mIntent.putExtra(getString(R.string.groupNum), groupNum);
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
}

