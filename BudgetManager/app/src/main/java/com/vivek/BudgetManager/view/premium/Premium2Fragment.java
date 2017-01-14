
package com.vivek.BudgetManager.view.premium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.BudgetManager.R;

/**
 * Fragment 2 of the premium onboarding screen
 *
 * @author Vivek Singh
 */
public class Premium2Fragment extends PremiumFragment
{
    public Premium2Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_premium2, container, false);
    }

}
