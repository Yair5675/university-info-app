package com.example.universities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    /**
     * A class for managing the selection of subjects in the application:
     */
    private class SubjectsHandler implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        private final String[] subjects; /* The names of all subjects available */
        private final TextView tvSubject; /* The textView that displays the chosen subject */

        public SubjectsHandler(Context context) {
            // Setting up the subject's listView:
            /* The listView that allows the user to choose a subject */
            ListView lvSubjects = findViewById(R.id.subjects_list);
            this.subjects = getResources().getStringArray(R.array.subjects);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, this.subjects);
            lvSubjects.setAdapter(adapter);

            lvSubjects.setOnItemClickListener(this);
            lvSubjects.setOnItemLongClickListener(this);

            // Setting up the textView that displays the chosen subject:
            this.tvSubject = findViewById(R.id.subject_tv);
            this.tvSubject.setText(subjects[0]);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
            // Changing the text of the textView presenting the subject to the clicked subject:
            this.tvSubject.setText(this.subjects[index]);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(MainActivity.this, this.subjects[i], Toast.LENGTH_SHORT).show();
            return true;
        }

        public String getCurrentSubject() {
            return this.tvSubject.getText().toString();
        }
    }


    /**
     * Class for handling how universities are shown on the screen and interacting with them:
     */
    private class UniversityHandler implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        private final ListView shownUnis;
        private UniversityAdapter adapter; /* The adapter for the listView of universities */
        private final ArrayList<University> astrophysics; /* List of all universities of astrophysics */
        private final ArrayList<University> criminology; /* List of all universities of criminology */
        private final ArrayList<University> history; /* List of all universities of history */
        private final ArrayList<University> nuclearPhysics; /* List of all universities of nuclear physics */
        private final ArrayList<University> philosophy; /* List of all universities of philosophy */
        private final ArrayList<University> theoreticalPhysics; /* List of all universities of theoretical physics */

        public UniversityHandler() {
            final AssetManager assets = getAssets();
            String[] subjects = getResources().getStringArray(R.array.subjects);
            final Resources res = getResources();
            final String packageName = getPackageName();

            // Loading the universities of each subject:
            this.astrophysics = University.loadCSV(assets, res, packageName, subjects[0]);
            this.criminology = University.loadCSV(assets, res, packageName, subjects[1]);
            this.history = University.loadCSV(assets, res, packageName, subjects[2]);
            this.nuclearPhysics = University.loadCSV(assets, res, packageName, subjects[3]);
            this.philosophy = University.loadCSV(assets, res, packageName, subjects[4]);
            this.theoreticalPhysics = University.loadCSV(assets, res, packageName, subjects[5]);

            /* The universities shown on the screen */
            this.shownUnis = findViewById(R.id.university_list);
            this.shownUnis.setOnItemClickListener(this);
            this.shownUnis.setOnItemLongClickListener(this);

            this.setShownUniversities(subjects[0]);
        }

        private ArrayList<University> getUniversities(String subject) {
            final String[] subjects = getResources().getStringArray(R.array.subjects);
            int index = -1;
            for (int i = 0; i < subjects.length; i++) {
                if (subjects[i].equals(subject)) {
                    index = i;
                    break;
                }
            }

            ArrayList<University> universities = null;
            switch (index) {
                case 0: {
                    universities = this.astrophysics;
                    break;
                }
                case 1: {
                    universities = this.criminology;
                    break;
                }
                case 2: {
                    universities = this.history;
                    break;
                }
                case 3: {
                    universities = this.nuclearPhysics;
                    break;
                }
                case 4: {
                    universities = this.philosophy;
                    break;
                }
                case 5: {
                    universities = this.theoreticalPhysics;
                    break;
                }

            }
            return universities;
        }

        public void setShownUniversities(String subject) {
            ArrayList<University> universities = this.getUniversities(subject);
            if (universities != null)
                this.adapter = new UniversityAdapter(getApplicationContext(), R.layout.university_adapter, universities, getResources(), getPackageName());
            this.shownUnis.setAdapter(this.adapter);
        }

        public void sortUniversities(double euRank, double worldRank, double acceptanceRate,
                                     double enrollment) {
            this.adapter.getUniversities().sort(Comparator.comparingDouble(u -> u.calculateValue(euRank, worldRank, acceptanceRate, enrollment)));

        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Getting the university that was clicked on:
            University clickedUni = this.adapter.getUniversities().get(i);
            // Launching the info-activity:
            launchInfo(clickedUni);
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(MainActivity.this, this.getUniversities(subjectsHandler.getCurrentSubject()).get(i).getName(), Toast.LENGTH_SHORT).show();
            return false;
        }

        /* A result activity launcher for going to the info activity: */
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        private void launchInfo(University university) {
            // Sending the university's information to the next activity:
            Bundle bundle = university.toBundle();
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtras(bundle);

            // Launching the activity:
            launcher.launch(intent);
        }
    }

    private SubjectsHandler subjectsHandler; /* An object that handles the selection of subjects */
    private UniversityHandler universityHandler; /* An object that handles the universities displayed on the screen */
    private ArrayList<AttributeLayout> attributes; /* The various attributes of a university that the user can prioritize */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the subject handler:
        this.subjectsHandler = new SubjectsHandler(this);

        // Setting up the university handler:
        this.universityHandler = new UniversityHandler();

        // Defining the attributes:
        this.attributes = new ArrayList<>();
        this.attributes.add(findViewById(R.id.eu_rank_attr));
        this.attributes.get(0).setText("Europe Rank");

        this.attributes.add(findViewById(R.id.world_rank_attr));
        this.attributes.get(1).setText("World Rank");

        this.attributes.add(findViewById(R.id.acceptance_rate_attr));
        this.attributes.get(2).setText("Acceptance");

        this.attributes.add(findViewById(R.id.enrollment_attr));
        this.attributes.get(3).setText("Enrollment");
    }

    public void searchUniversities(View view) {
        // Getting the preferences of the user:
        final double euRankImportance = this.attributes.get(0).getImportance() / 100.0;
        final double worldRankImportance = this.attributes.get(1).getImportance() / 100.0;
        final double acceptanceRateImportance = this.attributes.get(2).getImportance() / 100.0;
        final double enrollmentImportance = this.attributes.get(3).getImportance() / 100.0;

        // Sorting the universities on the screen:
        Log.i("subject", this.subjectsHandler.getCurrentSubject());
        this.universityHandler.setShownUniversities(this.subjectsHandler.getCurrentSubject());
        this.universityHandler.sortUniversities(euRankImportance, worldRankImportance, acceptanceRateImportance, enrollmentImportance);
    }


}