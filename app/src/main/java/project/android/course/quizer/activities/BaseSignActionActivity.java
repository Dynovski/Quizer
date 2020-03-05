package project.android.course.quizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;


// Activity providing basic ux utilities for signing in and signing up
public class BaseSignActionActivity extends AppCompatActivity
{
    public ProgressBar mProgressBar;

    public void setProgressBar(int resId)
    {
        mProgressBar = findViewById(resId);
    }

    public void showProgressBar()
    {
        if(mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar()
    {
        if(mProgressBar != null)
            mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void hideKeyboard(View view)
    {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        hideProgressBar();
    }
}
