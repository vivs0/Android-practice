
package com.vivek.BudgetManager.view.premium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vivek.BudgetManager.R;

/**
 * Fragment 3 of the premium onboarding screen
 *
 * @author Vivek Singh
 */
public class Premium3Fragment extends PremiumFragment
{
    public Premium3Fragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_premium3, container, false);
    }
}
