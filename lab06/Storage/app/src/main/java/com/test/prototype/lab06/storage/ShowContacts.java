package com.test.prototype.lab06.storage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class ShowContacts extends AppCompatActivity {

    private static final String contactListFileName = "contactData.txt";
    private static final int ADD_CONTACT_INTENT = 1;
    private static ArrayList<Contact> contacts;
    private static boolean deletingContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize arraylist of contacts
        contacts = new ArrayList<>();
        deletingContacts = false;

        // Add new contact button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(addContactClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read contacts from file and populate list
        System.out.println("Reading contacts from file...");
        try {
            FileInputStream raw = openFileInput(contactListFileName);
            Scanner in = new Scanner(raw);
            String line;
            while (in.hasNextLine()) {
                line = in.nextLine();
                System.out.println("Read Contact: " + line);
                String[] contactInfo = line.split(" ");
                // Add to arraylist
                contacts.add(new Contact(contactInfo[0], contactInfo[1], contactInfo[2]));
            }

            raw.close();
        } catch (FileNotFoundException e1) {
            System.err.println("Error reading from contacts file: Contacts file not found");
        } catch (IOException e2) {
            System.err.println("Error reading from contacts file: Failed to read contacts file");
        } catch (Exception e3) {
            System.err.println("Unknown error occurred while reading contacts:");
            e3.printStackTrace();
        }

        // Populate the ListView
        ListView contactList = (ListView)findViewById(R.id.listContacts);
        ContactsListAdapter contactAdapter = new ContactsListAdapter(this, contacts, deletingContacts);
        contactList.setAdapter(contactAdapter);

        // Handle when a contact is short tapped
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Select contact to delete
                if (deletingContacts) {
                    // Get the checkbox of the clicked contact
                    CheckBox contactCheckbox = (CheckBox) view.findViewById(R.id.checkBoxContact);

                    // Reverse the checkbox and clicked item check state.
                    if (contactCheckbox.isChecked()) {
                        contactCheckbox.setChecked(false);
                    } else {
                        contactCheckbox.setChecked(true);
                    }
                }
            }
        });

        // Handle when a contact is long tapped
        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deletingContacts = true;
                toggleContactDeleteMode();

                // Check long-pressed contact
                ((CheckBox)view.findViewById(R.id.checkBoxContact)).setChecked(true);

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Hide checkboxes beside each contact
        if (deletingContacts) {
            deletingContacts = false;
            toggleContactDeleteMode();
        }
    }

    private void showHideContactCheckBoxes() {
        // Show/hide checkboxes beside each contact (listview item)
        ListView contactList = (ListView)findViewById(R.id.listContacts);
        int visibility = (deletingContacts) ? View.VISIBLE : View.INVISIBLE;
        View listItem;
        CheckBox listItemCheckbox;
        for (int i = 0; i < contactList.getCount(); i++) {
            listItem = contactList.getAdapter().getView(i, contactList.getChildAt(i), contactList);
            listItemCheckbox = listItem.findViewById(R.id.checkBoxContact);
            listItemCheckbox.setVisibility(visibility);

            // Uncheck any checked checkboxes when hiding them
            if (!deletingContacts && listItemCheckbox.isChecked()) {
                listItemCheckbox.setChecked(false);
            }
        }
    }

    private void toggleContactDeleteMode() {
        showHideContactCheckBoxes();

        // Change floating action button icon to delete
        FloatingActionButton fab = findViewById(R.id.fab);
        if (deletingContacts) {
            fab.setOnClickListener(delContactClickListener());
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
        } else {
            fab.setOnClickListener(addContactClickListener());
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save contact data to local file
        System.out.println("Storing contacts to file...");
        try {
            FileOutputStream raw = openFileOutput(contactListFileName, Context.MODE_PRIVATE);

            // Continually remove first contact and store in contacts file
            Contact currContact;
            while (contacts.size() > 0) {
                currContact = contacts.remove(0);
                String contact = currContact.getFirstName() + " " +
                        currContact.getLastName() + " " + currContact.getPhone() + "\n";
                System.out.println("Storing contact: " + contact);
                raw.write(contact.getBytes());
            }

            raw.close();
        } catch (FileNotFoundException e1) {
            System.err.println("Error writing to contacts file: Contacts file not found");
        } catch (IOException e2) {
            System.err.println("Error writing to contacts file: Failed to write to contacts file");
        } catch (Exception e3) {
            System.err.println("Unknown error occurred while storing contacts:");
            e3.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_contacts, menu);
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

    private View.OnClickListener addContactClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactActivity = new Intent(ShowContacts.this, AddContact.class);
                startActivityForResult(addContactActivity, ADD_CONTACT_INTENT);
            }
        };
    }

    private View.OnClickListener delContactClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView contactList = (ListView)findViewById(R.id.listContacts);
                View listItem;
                ArrayList<Contact> contactsToDelete = new ArrayList<>();
                for (int i = 0; i < contactList.getCount(); i++) {
                    listItem = contactList.getAdapter().getView(i, contactList.getChildAt(i), contactList);

                    // Delete checked contacts from arraylist
                    if (((CheckBox)listItem.findViewById(R.id.checkBoxContact)).isChecked()) {
                        contactsToDelete.add(((Contact)contactList.getAdapter().getItem(i)));
                    }
                }

                // Show pop-up confirmation to delete contacts if at least one contact is selected
                if (contactsToDelete.size() > 0) {
                    showPopupDeleteConfirmation(contactList, contactsToDelete);
                }
            }
        };
    }

    private void showPopupDeleteConfirmation(final ListView contactList, final ArrayList<Contact> contactsToDelete) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ShowContacts.this);
        alert.setTitle("Confirm Delete");
        int numContactsToDelete = contactsToDelete.size();
        String deleteMessage = "Are you sure you want to delete the " + numContactsToDelete + " selected contacts?";
        if (numContactsToDelete == 1) {
            deleteMessage = "Are you sure you want to delete the selected contact?";
        }
        alert.setMessage(deleteMessage);
        alert.setNegativeButton("No", null);
        alert.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Remove contacts from arraylist
                while (contactsToDelete.size() > 0) {
                    contacts.remove(contactsToDelete.remove(0));
                }

                // Refresh listview to remove deleted contacts
                contactList.invalidateViews();

                // Uncheck checkboxes of items at previous indexes
                View listItem;
                for (int i = 0; i < contacts.size(); i++) {
                    listItem = contactList.getAdapter().getView(i, contactList.getChildAt(i), contactList);
                    ((CheckBox)listItem.findViewById(R.id.checkBoxContact)).setChecked(false);
                }
            }
        });
        alert.show();
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent resIntent) {
        super.onActivityResult(reqCode, resCode, resIntent);

        // Handle adding contact intent
        if (reqCode == ADD_CONTACT_INTENT && resCode == RESULT_OK) {
            // Get the information inputted by user
            String firstName = resIntent.getStringExtra("firstName");
            String lastName = resIntent.getStringExtra("lastName");
            String phoneNumber = resIntent.getStringExtra("phoneNum");

            // Add new contact and sort arraylist of contacts
            Contact newContact = new Contact(firstName, lastName, phoneNumber);
            contacts.add(contacts.size(), newContact);
            contacts.sort(new ContactComparator());
        }
    }

    private class ContactComparator implements Comparator<Contact> {

        public int compare(Contact contact1, Contact contact2) {
            // If first names are the same, compare last names
            if (contact1.getFirstName().equals(contact2.getFirstName())) {
                return contact1.getLastName().compareTo(contact2.getLastName());
            }

            // If first names are not the same, compare them
            return contact1.getFirstName().compareTo(contact2.getFirstName());
        }
    }
}
