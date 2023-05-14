package com.example.universities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UniversityAdapter extends ArrayAdapter<University> {
    private TextView uniName;
    private ImageView uniImage;
    private TextView uniLocation;
    private ArrayList<University> universities;
    private final Resources res;
    private final String packageName;

    public UniversityAdapter(@NonNull Context context, int resource, @NonNull ArrayList<University> items,
                             Resources res, String packageName) {
        super(context, resource, items);
        this.universities = items;
        this.res = res;
        this.packageName = packageName;
    }

    @NonNull
    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.university_adapter, parent, false);

        // Loading the image of the university and its name and location:
        this.uniImage = itemView.findViewById(R.id.uni_img);
        this.uniName = itemView.findViewById(R.id.uni_name);
        this.uniLocation = itemView.findViewById(R.id.uni_location);

        // Setting the image and the name based on the current object:
        this.uniImage.setImageDrawable(this.universities.get(position).getImage(this.res, this.packageName));
        this.uniName.setText(this.universities.get(position).getName());
        this.uniLocation.setText(this.universities.get(position).getLocation());
        this.uniName.setMaxLines(2);

        return itemView;
    }

    public ArrayList<University> getUniversities() {
        return universities;
    }
}
