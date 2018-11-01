package com.test.prototype.intents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static int LOGIN_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void showAbout(View view) {
        Intent showAboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(showAboutIntent);
    }

    public void attemptLogin(View view) {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_INTENT);
    }

    public void onActivityResult(int reqCode, int resCode, Intent resIntent) {
        super.onActivityResult(reqCode, resCode, resIntent);

        if (reqCode == LOGIN_INTENT) {
            String username = ((EditText)findViewById(R.id.textboxUsername)).getText().toString();
            String password = ((EditText)findViewById(R.id.textboxPassword)).getText().toString();
            Log.i("Intents", "User: " + username + " Pass: " + password);

            String toastMsg = "Login Failed";
            if (username.matches("(.*).(.*)") && password.length() == 9) {
                toastMsg = "Login Successful";
            }
            Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
        }

    }
}
