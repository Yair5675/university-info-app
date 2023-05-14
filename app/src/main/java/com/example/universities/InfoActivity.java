package com.example.universities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    @SuppressLint("DiscouragedApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Getting the university through the intent:
        final Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String subject = intent.getStringExtra("subject");
        String location = intent.getStringExtra("location");
        String url = intent.getStringExtra("url");
        int worldRank = intent.getIntExtra("world rank", 0);
        int euRank = intent.getIntExtra("eu rank", 0);
        double acceptance = intent.getDoubleExtra("acceptance", -1);
        int enrollment = intent.getIntExtra("enrollment", 0);
        final Resources res = getResources();

        University university = new University(name, subject, location, url, worldRank, euRank, acceptance, enrollment);

        init(university);
    }

    @SuppressLint("DefaultLocale")
    private void init(University university) {
        final String subject = university.getSubject();

        // Setting the title to the university's name:
        TextView tvTitle = findViewById(R.id.uni_name_info);
        tvTitle.setText(university.getName());
        tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Setting the university's location:
        TextView tvLocation = findViewById(R.id.location_tv_info);
        tvLocation.setText(university.getLocation());

        // Setting the europe rank:
        TextView tvEuRank = findViewById(R.id.europe_rank_tv);
        tvEuRank.setText(String.format(
                getString(R.string.europe_rank_txt), subject, university.getEuropeRank()
        ));

        // Setting the europe rank:
        TextView tvWorldRank = findViewById(R.id.world_rank_info);
        tvWorldRank.setText(String.format(
                getString(R.string.world_rank_txt), subject, university.getWorldRank()
        ));

        // Setting the enrollment:
        TextView tvEnrollment = findViewById(R.id.enrollment_info);
        tvEnrollment.setText(String.format(
                getString(R.string.enrollment_txt), university.getEnrollment()
        ));

        // Setting the acceptance rate (only if it's known):
        TextView tvAcceptance = findViewById(R.id.acceptance_tv_info);
        final double acceptance = university.getAcceptanceRate();
        if (acceptance > 0) {
            String acc = String.format("%d%%", (int)(acceptance * 100));
            tvAcceptance.setVisibility(View.VISIBLE);
            tvAcceptance.setText(String.format(
                    getString(R.string.acceptance_txt), acc
            ));
        }
        else
            tvAcceptance.setVisibility(View.GONE);

        // Making the URL open the university's page upon being clicked:
        TextView tvURL = findViewById(R.id.url_tv_info);
        tvURL.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(university.getUrl()));
            startActivity(intent);
        });

        // Setting the image of the university:
        ImageView uniImage = findViewById(R.id.uni_img_info);
        uniImage.setImageDrawable(university.getImage(getResources(), getPackageName()));
    }
}