package com.test.prototype.lab06.storage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
    }

    public void addContact(View view) {
        String firstName = ((EditText) findViewById(R.id.textfieldFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.textfieldLastName)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.textfieldPhoneNum)).getText().toString();

        // Check if all fields are empty or not
        if (firstName.equals("") || lastName.equals("") || phoneNumber.equals("")) {
            Toast.makeText(AddContact.this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent(Intent.ACTION_PICK);
            resultIntent.putExtra("firstName", firstName);
            resultIntent.putExtra("lastName", lastName);
            resultIntent.putExtra("phoneNum", phoneNumber);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    public void cancelAddContact(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

}
