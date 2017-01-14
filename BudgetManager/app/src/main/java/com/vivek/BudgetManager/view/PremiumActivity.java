

package com.vivek.BudgetManager.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vivek.BudgetManager.BudgetManager;
import com.vivek.BudgetManager.PremiumPurchaseListener;
import com.vivek.BudgetManager.R;
import com.vivek.BudgetManager.view.premium.Premium1Fragment;
import com.vivek.BudgetManager.view.premium.Premium2Fragment;
import com.vivek.BudgetManager.view.premium.Premium3Fragment;

import me.relex.circleindicator.CircleIndicator;

/**
 * Activity that contains the premium onboarding screen. This activity should return with a
 * {@link Activity#RESULT_OK} if user has successfully purchased premium.
 *
 * @author Vivek Singh
 */
public class PremiumActivity extends AppCompatActivity
{
    /**
     * The view pager
     */
    private ViewPager pager;

// ------------------------------------->

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        // Cancelled by default
        setResult(Activity.RESULT_CANCELED);

        pager = (ViewPager) findViewById(R.id.premium_view_pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public Fragment getItem(int position)
            {
                switch (position)
                {
                    case 0:
                        return new Premium1Fragment();
                    case 1:
                        return new Premium2Fragment();
                    case 2:
                        return new Premium3Fragment();
                }

                return null;
            }

            @Override
            public int getCount()
            {
                return 3;
            }
        });
        pager.setOffscreenPageLimit(pager.getAdapter().getCount()); // preload all fragments for transitions smoothness

        // Circle indicator
        ((CircleIndicator) findViewById(R.id.premium_view_pager_indicator)).setViewPager(pager);

        findViewById(R.id.premium_not_now_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        findViewById(R.id.premium_cta_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Show loader
                final ProgressDialog loading = ProgressDialog.show(PremiumActivity.this,
                        getResources().getString(R.string.iab_purchase_wait_title),
                        getResources().getString(R.string.iab_purchase_wait_message),
                        true, false);

                ((BudgetManager) getApplication()).launchPremiumPurchaseFlow(PremiumActivity.this, new PremiumPurchaseListener()
                {
                    @Override
                    public void onUserCancelled()
                    {
                        loading.dismiss();
                    }

                    @Override
                    public void onPurchaseError(String error)
                    {
                        loading.dismiss();

                        new AlertDialog.Builder(PremiumActivity.this)
                            .setTitle(R.string.iab_purchase_error_title)
                            .setMessage(getResources().getString(R.string.iab_purchase_error_message, error))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    }

                    @Override
                    public void onPurchaseSuccess()
                    {
                        loading.dismiss();
                        setResult(Activity.RESULT_OK); // Important to update the UI
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if( pager.getCurrentItem() > 0 )
        {
            pager.setCurrentItem(pager.getCurrentItem()-1, true);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // iab management
        if( !((BudgetManager) getApplication()).handleActivityResult(requestCode, resultCode, data) )
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
