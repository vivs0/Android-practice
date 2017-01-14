
package com.vivek.BudgetManager.view.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.BudgetManager.R;

/**
 * Onboarding step 1 fragment
 *
 * @author Vivek Singh
 */
public class Onboarding1Fragment extends OnboardingFragment
{
    /**
     * Required empty public constructor
     */
    public Onboarding1Fragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_onboarding1, container, false);

        v.findViewById(R.id.onboarding_screen1_next_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                next(v);
            }
        });

        return v;
    }


    @Override
    public int getStatusBarColor()
    {
        return R.color.primary_dark;
    }
}
