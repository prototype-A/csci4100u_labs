package com.test.prototype.lab07.databaseaccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class GradeListAdapter extends ArrayAdapter<Grade> {

    private Context context;
    private List<Grade> data;
    private boolean showCheckBoxes;

    public GradeListAdapter(Context context, List<Grade> data, boolean showCheckBoxes) {
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
            listItem = LayoutInflater.from(context).inflate(R.layout.grade_list_item, parent, false);
        }

        // Collect data to be inserted
        Grade grade = data.get(position);

        // Populate the UI
        TextView sid = listItem.findViewById(R.id.labelStudentId);
        sid.setText(String.valueOf(grade.getStudentId()));

        TextView course = listItem.findViewById(R.id.labelCourseComponent);
        course.setText(grade.getCourseComponent());

        TextView mark = listItem.findViewById(R.id.labelMark);
        mark.setText(String.valueOf(grade.getMark()));

        CheckBox checkbox = listItem.findViewById(R.id.checkBoxGrade);
        if (showCheckBoxes) {
            checkbox.setVisibility(View.VISIBLE);
        }

        return listItem;
    }
}

