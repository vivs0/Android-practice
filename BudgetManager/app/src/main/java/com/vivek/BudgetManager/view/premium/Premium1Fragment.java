
package com.vivek.BudgetManager.view.premium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.BudgetManager.R;

/**
 * Fragment 1 of the premium onboarding screen
 *
 * @author Vivek Singh
 */
public class Premium1Fragment extends PremiumFragment
{

    public Premium1Fragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_premium1, container, false);
    }

}
