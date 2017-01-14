
package com.vivek.BudgetManager.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.vivek.BudgetManager.R;
import com.vivek.BudgetManager.helper.Logger;
import com.vivek.BudgetManager.helper.UIHelper;
import com.vivek.BudgetManager.helper.CurrencyHelper;
import com.vivek.BudgetManager.model.Expense;
import com.vivek.BudgetManager.model.MonthlyExpense;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthlyExpenseEditActivity extends DBActivity
{
    /**
     * Save floating action button
     */
    private FloatingActionButton fab;
    /**
     * Edit text that contains the description
     */
    private EditText             descriptionEditText;
    /**
     * Edit text that contains the amount
     */
    private EditText             amountEditText;
    /**
     * Button for date selection
     */
    private Button               dateButton;
    /**
     * Textview that displays the type of expense
     */
    private TextView             expenseType;

    /**
     * Expense that is being edited (will be null if it's a new one)
     */
    private MonthlyExpense expense;
    /**
     * The start date of the expense
     */
    private Date           dateStart;
    /**
     * The end date of the expense
     */
    private Date           dateEnd;
    /**
     * Is the new expense a revenue
     */
    private boolean isRevenue = false;


// ------------------------------------------->

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_expense_edit);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateStart = (Date) getIntent().getSerializableExtra("dateStart");
        dateEnd = (Date) getIntent().getSerializableExtra("dateEnd");

        if (getIntent().hasExtra("expense"))
        {
            expense = (MonthlyExpense) getIntent().getSerializableExtra("expense");

            setTitle(R.string.title_activity_monthly_expense_edit);
        }

        setUpButtons();
        setUpTextFields();
        setUpDateButton();

        setResult(RESULT_CANCELED);

        if ( UIHelper.willAnimateActivityEnter(this) )
        {
            UIHelper.animateActivityEnter(this, new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    UIHelper.setFocus(descriptionEditText);
                    UIHelper.showFAB(fab);
                }
            });
        }
        else
        {
            UIHelper.setFocus(descriptionEditText);
            UIHelper.showFAB(fab);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id == android.R.id.home ) // Back button of the actionbar
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

// ----------------------------------->

    /**
     * Validate user inputs
     *
     * @return true if user inputs are ok, false otherwise
     */
    private boolean validateInputs()
    {
        boolean ok = true;

        String description = descriptionEditText.getText().toString();
        if( description.trim().isEmpty() )
        {
            descriptionEditText.setError(getResources().getString(R.string.no_description_error));
            ok = false;
        }

        String amount = amountEditText.getText().toString();
        if( amount.trim().isEmpty() )
        {
            amountEditText.setError(getResources().getString(R.string.no_amount_error));
            ok = false;
        }
        else
        {
            try
            {
                double value = Double.parseDouble(amount);
                if( value <= 0 )
                {
                    amountEditText.setError(getResources().getString(R.string.negative_amount_error));
                    ok = false;
                }
            }
            catch(Exception e)
            {
                amountEditText.setError(getResources().getString(R.string.invalid_amount));
                ok = false;
            }
        }

        return ok;
    }

    /**
     * Set-up revenue and payment buttons
     */
    private void setUpButtons()
    {
        expenseType = (TextView) findViewById(R.id.expense_type_tv);

        SwitchCompat expenseTypeSwitch = (SwitchCompat) findViewById(R.id.expense_type_switch);
        expenseTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                isRevenue = isChecked;
                setExpenseTypeTextViewLayout();
            }
        });

        // Init value to checked if already a revenue (can be true if we are editing an expense)
        if( isRevenue )
        {
            expenseTypeSwitch.setChecked(true);
            setExpenseTypeTextViewLayout();
        }

        fab = (FloatingActionButton) findViewById(R.id.save_expense_fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( validateInputs() )
                {
                    double value = Double.parseDouble(amountEditText.getText().toString());

                    MonthlyExpense expense = new MonthlyExpense(descriptionEditText.getText().toString(), isRevenue? -value : value, dateStart);

                    new SaveMonthlyExpenseTask().execute(expense);
                }
            }
        });
    }

    /**
     * Set revenue text view layout
     */
    private void setExpenseTypeTextViewLayout()
    {
        if( isRevenue )
        {
            expenseType.setText(R.string.income);
            expenseType.setTextColor(ContextCompat.getColor(this, R.color.budget_green));

            setTitle(R.string.title_activity_monthly_income_add);
        }
        else
        {
            expenseType.setText(R.string.payment);
            expenseType.setTextColor(ContextCompat.getColor(this, R.color.budget_red));

            setTitle(R.string.title_activity_monthly_expense_add);
        }
    }

    /**
     * Set up text field focus behavior
     */
    private void setUpTextFields()
    {
        ((TextInputLayout) findViewById(R.id.amount_inputlayout)).setHint(getResources().getString(R.string.amount, CurrencyHelper.getUserCurrency(this).getSymbol()));

        descriptionEditText = (EditText) findViewById(R.id.description_edittext);

        if( expense != null )
        {
            descriptionEditText.setText(expense.getTitle());
            descriptionEditText.setSelection(descriptionEditText.getText().length()); // Put focus at the end of the text
        }

        amountEditText = (EditText) findViewById(R.id.amount_edittext);
        UIHelper.preventUnsupportedInputForDecimals(amountEditText);

        if( expense != null )
        {
            amountEditText.setText(CurrencyHelper.getFormattedAmountValue(Math.abs(expense.getAmount())));
        }
    }

    /**
     * Set up the date button
     */
    private void setUpDateButton()
    {
        dateButton = (Button) findViewById(R.id.date_button);
        UIHelper.removeButtonBorder(dateButton); // Remove border on lollipop

        updateDateButtonDisplay();

        dateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            DatePickerDialogFragment fragment = new DatePickerDialogFragment(dateStart, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dateStart = cal.getTime();
                updateDateButtonDisplay();
                }
            });

            fragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private void updateDateButtonDisplay()
    {
        SimpleDateFormat formatter = new SimpleDateFormat(getResources().getString(R.string.add_expense_date_format), Locale.getDefault());
        dateButton.setText(formatter.format(dateStart));
    }

// ------------------------------------------->

    /**
     * An asynctask to save monthly expense to DB
     */
    private class SaveMonthlyExpenseTask extends AsyncTask<MonthlyExpense, Integer, Boolean>
    {
        /**
         * Dialog used to display loading to the user
         */
        private ProgressDialog dialog;

        @Override
        protected Boolean doInBackground(MonthlyExpense... expenses)
        {
            for (MonthlyExpense expense : expenses)
            {
                boolean inserted = db.addMonthlyExpense(expense);
                if( !inserted )
                {
                    Logger.error(false, "Error while inserting monthly expense into DB: addMonthlyExpense returned false");
                    return false;
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateStart);

                // Add up to 10 years of expenses
                for (int i = 0; i < 12 * 10; i++)
                {
                    boolean expenseInserted = db.persistExpense(new Expense(expense.getTitle(), expense.getAmount(), cal.getTime(), expense.getId()));
                    if (!expenseInserted)
                    {
                        Logger.error(false, "Error while inserting expense for monthly expense into DB: persistExpense returned false");
                        return false;
                    }

                    cal.add(Calendar.MONTH, 1);

                    if (dateEnd != null && cal.getTime().after(dateEnd)) // If we have an end date, stop to that one
                    {
                        break;
                    }
                }
            }

            return true;
        }

        @Override
        protected void onPreExecute()
        {
            // Show a ProgressDialog
            dialog = new ProgressDialog(MonthlyExpenseEditActivity.this);
            dialog.setIndeterminate(true);
            dialog.setTitle(R.string.monthly_expense_add_loading_title);
            dialog.setMessage(getResources().getString(isRevenue ? R.string.monthly_income_add_loading_message : R.string.monthly_expense_add_loading_message));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            // Dismiss the dialog
            dialog.dismiss();

            if (result)
            {
                setResult(RESULT_OK);
                finish();
            }
            else
            {
                new AlertDialog.Builder(MonthlyExpenseEditActivity.this)
                    .setTitle(R.string.monthly_expense_add_error_title)
                    .setMessage(getResources().getString(R.string.monthly_expense_add_error_message))
                    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    })
                    .show();
            }
        }
    }
}
