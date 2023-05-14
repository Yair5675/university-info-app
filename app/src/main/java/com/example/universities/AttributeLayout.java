package com.example.universities;

import android.content.Context;

import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

public class AttributeLayout extends ConstraintLayout {

    private TextView attribute; /* The textView that will display the attribute's name */
    private SeekBar importance; /* A seekbar that will determine the attribute's importance */
    private static final int MAX_IMPORTANCE = 100; /* The maximum importance on the seekbar */
    private static final int SEPARATION_MARGIN = 16; /* The margin between the seekbar and the textView */

    public AttributeLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public AttributeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttributeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initializing the attribute (the textView) and its importance (the seekbar):
        this.attribute = new TextView(this.getContext());
        this.attribute.setId(generateViewId());

        this.importance = new SeekBar(this.getContext());
        this.importance.setId(generateViewId());

        // Setting the location of the attribute in the layout:
        LayoutParams attributeParams = new LayoutParams (
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        // Putting the textView at the top-middle of the layout:
        attributeParams.topToTop = LayoutParams.PARENT_ID;
        attributeParams.startToStart = LayoutParams.PARENT_ID;
        attributeParams.endToEnd = LayoutParams.PARENT_ID;

        this.attribute.setLayoutParams(attributeParams);

        // Setting the location of the importance seekbar in the layout:
        LayoutParams importanceParams = new Constraints.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Putting the seekbar below the textview (but with a margin):
        importanceParams.topToBottom = this.attribute.getId();
        importanceParams.topMargin = SEPARATION_MARGIN;
        importanceParams.startToStart = LayoutParams.PARENT_ID;
        importanceParams.endToEnd = LayoutParams.PARENT_ID;
        importanceParams.width = LayoutParams.MATCH_CONSTRAINT;

        this.importance.setLayoutParams(importanceParams);
        this.importance.setMax(MAX_IMPORTANCE);
        this.setImportance(50);

        // Adding both views to the layout:
        this.addView(this.attribute);
        this.addView(this.importance);
    }

    public void setText(String attribute) {
        this.attribute.setText(attribute);
    }

    public void setTextSize(final float SIZE) {
        this.attribute.setTextSize(SIZE);
    }

    public void setImportance(int progress) {
        this.importance.setProgress(progress);
    }

    public int getImportance() {
        return this.importance.getProgress();
    }
}
