package com.example.universities;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class University {
    private final String name;
    private final String subject;
    private final String location;
    private final String url;
    private final int worldRank;
    private final int europeRank;
    private final double acceptanceRate;
    private final int enrollment;
    private final String imageName;


    public University(String name, String subject, String location, String url, int worldRank, int europeRank, double acceptanceRate, int enrollment) {
        this.name = name;
        this.subject = subject;
        this.location = location;
        this.url = url;
        this.worldRank = worldRank;
        this.europeRank = europeRank;
        this.acceptanceRate = acceptanceRate;
        this.enrollment = enrollment;
        this.imageName = getImageName(this.subject, this.name);
    }

    private static String getImageName(String subject, String uniName) {
        return String.format(
                "%s_%s_logo",
                subject.toLowerCase().replace(" ", "_"),
                uniName.toLowerCase().replace(" ", "_")
                                     .replace("-", "_")
                                     .replace("'", "")
        );
    }

    @SuppressLint("DiscouragedApi")
    public static ArrayList<University> loadCSV(AssetManager assets, Resources res, String packageName, String subjectName) {
        ArrayList<University> universities = new ArrayList<>();
        final String CSV_PATH = subjectName.toLowerCase() + ".csv";

        try {
            // Loading the csv:
            Scanner reader = new Scanner(assets.open(CSV_PATH));
            String name, subject, location, url;
            int worldRank, europeRank, enrollment;
            double acceptanceRate;
            String[] attributes;

            // Looping over each line (after skipping the first one):
            reader.nextLine();
            while (reader.hasNext()) {
                // Separating the line into the various attributes of the csv:
                attributes = reader.nextLine().split(",");

                Log.i("Attributes", Arrays.toString(attributes));

                // Loading the values from the line:
                name = attributes[0];
                subject = attributes[1];
                location = attributes[2];
                url = attributes[3];
                worldRank = Integer.parseInt(attributes[4]);
                europeRank = Integer.parseInt(attributes[5]);
                acceptanceRate = Double.parseDouble(attributes[6]);
                enrollment = Integer.parseInt(attributes[7]);

                // Getting the image:
                final String IMAGE_NAME = getImageName(subjectName, name);
                final int ID = res.getIdentifier(IMAGE_NAME, "drawable", packageName);
                Drawable image = ResourcesCompat.getDrawable(res, ID, null);

                // Loading the data into a University object and adding it to the list:
                universities.add(new University(name, subject, location, url, worldRank, europeRank, acceptanceRate, enrollment));

            }
        } catch (IOException e) {
            throw new RuntimeException("Could not open csv: " + CSV_PATH);
        }

        return universities;
    }

    /**
     * Given various measurements of certain attributes, the function returns the value of the
     * university. For example, if the importance of 'europeRank' is 0.5 and the importance of
     * 'worldRank' is 1, universities with high world rank will have more value than ones with high
     * europe rank.
     */
    public double calculateValue(double europeRank, double worldRank, double acceptanceRate,
                                 double enrollment) {
        return europeRank * this.europeRank +
                worldRank * this.worldRank +
                (this.acceptanceRate != -1 ? acceptanceRate * this.acceptanceRate : 0) +
                enrollment * this.enrollment;
    }


    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public int getWorldRank() {
        return worldRank;
    }

    public int getEuropeRank() {
        return europeRank;
    }

    public double getAcceptanceRate() {
        return acceptanceRate;
    }

    public int getEnrollment() {
        return enrollment;
    }

    public Drawable getImage(final Resources res, final String packageName) {
        final int ID = res.getIdentifier(this.imageName, "drawable", packageName);
        Log.d("id", this.imageName);
        return ResourcesCompat.getDrawable(res, ID, null);
    }

    public Bundle toBundle() {
        // Breaking the information down to the bundle:
        Bundle bundle = new Bundle();
        bundle.putString("name", this.getName());
        bundle.putString("subject", this.getSubject());
        bundle.putString("location", this.getLocation());
        bundle.putString("url", this.getUrl());
        bundle.putInt("world rank", this.getWorldRank());
        bundle.putInt("eu rank", this.getEuropeRank());
        bundle.putDouble("acceptance", this.getAcceptanceRate());
        bundle.putInt("enrollment", this.getEnrollment());
        bundle.putString("image name", this.imageName);
        Log.d("image name", this.imageName);

        return bundle;
    }
}
