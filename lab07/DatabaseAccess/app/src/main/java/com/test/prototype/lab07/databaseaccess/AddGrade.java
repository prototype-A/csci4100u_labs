package com.test.prototype.lab07.databaseaccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddGrade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);
    }

    public void addGrade(View view) {
        String sid = ((EditText) findViewById(R.id.textfieldStudentId)).getText().toString();
        String courseComponent = ((EditText) findViewById(R.id.textfieldCourseComponent)).getText().toString();
        String mark = ((EditText) findViewById(R.id.textfieldMark)).getText().toString();

        // Check if all fields are empty or not
        if (sid.equals("") || courseComponent.equals("") || mark.equals("")) {
            Toast.makeText(AddGrade.this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent(Intent.ACTION_PICK);
            resultIntent.putExtra("studentId", Integer.valueOf(sid));
            resultIntent.putExtra("courseComponent", courseComponent);
            resultIntent.putExtra("mark", Float.valueOf(mark));
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    public void cancelAddGrade(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

}
