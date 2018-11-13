package com.test.prototype.lab06.storage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ContactsListAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> data;
    private boolean showCheckBoxes;

    public ContactsListAdapter(Context context, List<Contact> data, boolean showCheckBoxes) {
        super(context, 0, data);

        this.context = context;
        this.data = data;
        this.showCheckBoxes = showCheckBoxes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Potentially reuse the list item view
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false);
        }

        // Collect data to be inserted
        Contact contact = data.get(position);

        // Populate the UI
        TextView name = listItem.findViewById(R.id.labelName);
        name.setText(contact.getFirstName() + " " + contact.getLastName());

        TextView phone = listItem.findViewById(R.id.labelPhone);
        phone.setText(contact.getPhone());

        CheckBox checkbox = listItem.findViewById(R.id.checkBoxContact);
        if (showCheckBoxes) {
            checkbox.setVisibility(View.VISIBLE);
        }

        return listItem;
    }
}
