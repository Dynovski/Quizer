package project.android.course.quizer;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

public class UncheckableRadioButton extends AppCompatRadioButton
{

    public UncheckableRadioButton(Context context)
    {
        super(context);
    }

    public UncheckableRadioButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public UncheckableRadioButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle()
    {

        if(isChecked())
            setChecked(false);
        else
            super.toggle();
    }

    @Override
    public CharSequence getAccessibilityClassName()
    {
        return UncheckableRadioButton.class.getName();
    }
}