
package com.vivek.BudgetManager.view.welcome;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.BudgetManager.R;

/**
 * Onboarding step 4 fragment
 *
 * @author Vivek Singh
 */
public class Onboarding4Fragment extends OnboardingFragment
{

    public Onboarding4Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_onboarding4, container, false);

        v.findViewById(R.id.onboarding_screen4_next_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                done();
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
