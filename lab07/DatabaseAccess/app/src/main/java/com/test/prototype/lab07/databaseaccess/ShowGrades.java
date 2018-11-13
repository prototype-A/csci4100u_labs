package com.test.prototype.lab07.databaseaccess;

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

import java.util.ArrayList;
import java.util.List;

public class ShowGrades extends AppCompatActivity {

    private static final int ADD_GRADE_INTENT = 1;
    private static GradesDBHelper db;
    private static List<Grade> grades;
    private static boolean deletingGrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_grades);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize
        db = new GradesDBHelper(getApplicationContext());
        deletingGrades = false;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(addGradeClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read grades from database
        grades = db.getAllGrades();

        // Populate the ListView
        ListView contactList = (ListView)findViewById(R.id.listGrades);
        GradeListAdapter contactAdapter = new GradeListAdapter(this, grades, deletingGrades);
        contactList.setAdapter(contactAdapter);

        // Handle when a contact is short tapped
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Select contact to delete
                if (deletingGrades) {
                    // Get the checkbox of the clicked contact
                    CheckBox contactCheckbox = (CheckBox) view.findViewById(R.id.checkBoxGrade);

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
                deletingGrades = true;
                toggleGradeDeleteMode();

                // Check long-pressed contact
                ((CheckBox)view.findViewById(R.id.checkBoxGrade)).setChecked(true);

                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        // Hide checkboxes beside each contact
        if (deletingGrades) {
            deletingGrades = false;
            toggleGradeDeleteMode();
        }
    }

    private void showHideGradeCheckBoxes() {
        // Show/hide checkboxes beside each contact (listview item)
        ListView contactList = (ListView)findViewById(R.id.listGrades);
        int visibility = (deletingGrades) ? View.VISIBLE : View.INVISIBLE;
        View listItem;
        CheckBox listItemCheckbox;
        for (int i = 0; i < contactList.getCount(); i++) {
            listItem = contactList.getAdapter().getView(i, contactList.getChildAt(i), contactList);
            listItemCheckbox = listItem.findViewById(R.id.checkBoxGrade);
            listItemCheckbox.setVisibility(visibility);

            // Uncheck any checked checkboxes when hiding them
            if (!deletingGrades && listItemCheckbox.isChecked()) {
                listItemCheckbox.setChecked(false);
            }
        }
    }

    private void toggleGradeDeleteMode() {
        showHideGradeCheckBoxes();

        // Change floating action button icon to delete
        FloatingActionButton fab = findViewById(R.id.fab);
        if (deletingGrades) {
            fab.setOnClickListener(delGradeClickListener());
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
        } else {
            fab.setOnClickListener(addGradeClickListener());
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_input_add));
        }
    }

    private View.OnClickListener addGradeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactActivity = new Intent(ShowGrades.this, AddGrade.class);
                startActivityForResult(addContactActivity, ADD_GRADE_INTENT);
            }
        };
    }

    private View.OnClickListener delGradeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView contactList = (ListView)findViewById(R.id.listGrades);
                View listItem;
                ArrayList<Grade> gradesToDelete = new ArrayList<>();
                for (int i = 0; i < contactList.getCount(); i++) {
                    listItem = contactList.getAdapter().getView(i, contactList.getChildAt(i), contactList);

                    // Delete checked contacts from arraylist
                    if (((CheckBox)listItem.findViewById(R.id.checkBoxGrade)).isChecked()) {
                        gradesToDelete.add(((Grade)contactList.getAdapter().getItem(i)));
                    }
                }

                // Show pop-up confirmation to delete contacts if at least one contact is selected
                if (gradesToDelete.size() > 0) {
                    showPopupDeleteConfirmation(contactList, gradesToDelete);
                }
            }
        };
    }

    private void showPopupDeleteConfirmation(final ListView gradeList, final ArrayList<Grade> gradesToDelete) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ShowGrades.this);
        alert.setTitle("Confirm Delete");
        int numContactsToDelete = gradesToDelete.size();
        String deleteMessage = "Are you sure you want to delete the " + numContactsToDelete + " selected grades?";
        if (numContactsToDelete == 1) {
            deleteMessage = "Are you sure you want to delete the selected grade?";
        }
        alert.setMessage(deleteMessage);
        alert.setNegativeButton("No", null);
        alert.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Remove contacts from arraylist
                while (gradesToDelete.size() > 0) {
                    Grade gradeToDel = gradesToDelete.remove(0);
                    grades.remove(gradeToDel);
                    db.deleteGrade(gradeToDel);
                }

                // Refresh listview to remove deleted contacts
                gradeList.invalidateViews();

                // Uncheck checkboxes of items at previous indexes
                View listItem;
                for (int i = 0; i < grades.size(); i++) {
                    listItem = gradeList.getAdapter().getView(i, gradeList.getChildAt(i), gradeList);
                    ((CheckBox)listItem.findViewById(R.id.checkBoxGrade)).setChecked(false);
                }
            }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_grades, menu);
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

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent resIntent) {
        super.onActivityResult(reqCode, resCode, resIntent);

        // Handle adding contact intent
        if (reqCode == ADD_GRADE_INTENT && resCode == RESULT_OK) {
            // Get the information inputted by user
            int sid = resIntent.getIntExtra("studentId", 0);
            String courseComponent = resIntent.getStringExtra("courseComponent");
            float mark = resIntent.getFloatExtra("mark", 0.0f);

            // Add new contact
            grades.add(db.addGrade(sid, courseComponent, mark));
        }
    }

}
