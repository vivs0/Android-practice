
package com.vivek.BudgetManager.view.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vivek.BudgetManager.R;
import com.vivek.BudgetManager.helper.CurrencyHelper;
import com.vivek.BudgetManager.model.Expense;
import com.vivek.BudgetManager.model.MonthlyExpenseDeleteType;
import com.vivek.BudgetManager.model.db.DB;
import com.vivek.BudgetManager.view.ExpenseEditActivity;
import com.vivek.BudgetManager.view.MainActivity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Recycler view adapter to display expenses for a given date
 *
 * @author vivek singh
 */
public class ExpensesRecyclerViewAdapter extends RecyclerView.Adapter<ExpensesRecyclerViewAdapter.ViewHolder>
{
    private List<Expense> expenses;
    private Date date;
    private Activity activity;

// ------------------------------------------->

    /**
     * Instanciate an adapter for the given date
     *
     * @param activity
     * @param db
     * @param date
     */
    public ExpensesRecyclerViewAdapter(@NonNull Activity activity, @NonNull DB db, @NonNull Date date)
    {
        this.activity = activity;
        this.date = date;
        this.expenses = db.getExpensesForDay(date);
    }

    /**
     * Return the date content is displayed for displayed
     *
     * @return
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Set a new date to display
     *
     * @param date
     * @param db
     */
    public void setDate(@NonNull Date date, @NonNull DB db)
    {
        this.date = date;
        this.expenses = db.getExpensesForDay(date);
        notifyDataSetChanged();
    }

    /**
     * Remove given expense
     *
     * @param expense
     * @return position of the deleted expense (-1 if not found)
     */
    public int removeExpense(Expense expense)
    {
        Iterator<Expense> expenseIterator = expenses.iterator();
        int position = 0;
        while( expenseIterator.hasNext() )
        {
            Expense shownExpense = expenseIterator.next();
            if( shownExpense.getId().equals(expense.getId()) )
            {
                expenseIterator.remove();
                notifyItemRemoved(position);
                return position;
            }

            position++;
        }

        return -1;
    }

    /**
     * Add an expense at the given position
     *
     * @param expense
     * @param position
     */
    public void addExpense(Expense expense, int position)
    {
        expenses.add(position, expense);
        notifyItemRangeInserted(position, 1);
    }

// ------------------------------------------>

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycleview_expense_cell, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i)
    {
        final Expense expense = expenses.get(i);

        viewHolder.expenseTitleTextView.setText(expense.getTitle());
        viewHolder.expenseAmountTextView.setText(CurrencyHelper.getFormattedCurrencyString(viewHolder.view.getContext(), -expense.getAmount()));
        viewHolder.expenseAmountTextView.setTextColor(ContextCompat.getColor(viewHolder.view.getContext(), expense.isRevenue() ? R.color.budget_green : R.color.budget_red));
        viewHolder.monthlyIndicator.setVisibility(expense.isMonthly() ? View.VISIBLE : View.GONE);
        viewHolder.positiveIndicator.setImageResource(expense.isRevenue() ? R.drawable.ic_label_green : R.drawable.ic_label_red);

        final View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (expense.isMonthly())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(expense.isRevenue() ? R.string.dialog_edit_monthly_income_title : R.string.dialog_edit_monthly_expense_title);
                    builder.setItems(expense.isRevenue() ? R.array.dialog_edit_monthly_income_choices : R.array.dialog_edit_monthly_expense_choices, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0: // Edit this one
                                {
                                    Intent startIntent = new Intent(viewHolder.view.getContext(), ExpenseEditActivity.class);
                                    startIntent.putExtra("date", expense.getDate());
                                    startIntent.putExtra("expense", expense);

                                    ActivityCompat.startActivityForResult(activity, startIntent, MainActivity.ADD_EXPENSE_ACTIVITY_CODE, null);

                                    break;
                                }
                                case 1: // Delete this one
                                {
                                    // Send notification to inform views that this expense has been deleted
                                    Intent intent = new Intent(MainActivity.INTENT_MONTHLY_EXPENSE_DELETED);
                                    intent.putExtra("expense", expense);
                                    intent.putExtra("deleteType", MonthlyExpenseDeleteType.ONE.getValue());
                                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);

                                    break;
                                }
                                case 2: // Delete from
                                {
                                    // Send notification to inform views that this expense has been deleted
                                    Intent intent = new Intent(MainActivity.INTENT_MONTHLY_EXPENSE_DELETED);
                                    intent.putExtra("expense", expense);
                                    intent.putExtra("deleteType", MonthlyExpenseDeleteType.FROM.getValue());
                                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);

                                    break;
                                }
                                case 3: // Delete up to
                                {
                                    // Send notification to inform views that this expense has been deleted
                                    Intent intent = new Intent(MainActivity.INTENT_MONTHLY_EXPENSE_DELETED);
                                    intent.putExtra("expense", expense);
                                    intent.putExtra("deleteType", MonthlyExpenseDeleteType.TO.getValue());
                                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);

                                    break;
                                }
                                case 4: // Delete all
                                {
                                    // Send notification to inform views that this expense has been deleted
                                    Intent intent = new Intent(MainActivity.INTENT_MONTHLY_EXPENSE_DELETED);
                                    intent.putExtra("expense", expense);
                                    intent.putExtra("deleteType", MonthlyExpenseDeleteType.ALL.getValue());
                                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);

                                    break;
                                }
                            }
                        }
                    });
                    builder.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(expense.isRevenue() ? R.string.dialog_edit_income_title : R.string.dialog_edit_expense_title);
                    builder.setItems(expense.isRevenue() ? R.array.dialog_edit_income_choices : R.array.dialog_edit_expense_choices, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case 0: // Edit expense
                                {
                                    Intent startIntent = new Intent(viewHolder.view.getContext(), ExpenseEditActivity.class);
                                    startIntent.putExtra("date", expense.getDate());
                                    startIntent.putExtra("expense", expense);

                                    ActivityCompat.startActivityForResult(activity, startIntent, MainActivity.ADD_EXPENSE_ACTIVITY_CODE, null);

                                    break;
                                }
                                case 1: // Delete
                                {
                                    // Send notification to inform views that this expense has been deleted
                                    Intent intent = new Intent(MainActivity.INTENT_EXPENSE_DELETED);
                                    intent.putExtra("expense", expense);
                                    LocalBroadcastManager.getInstance(activity.getApplicationContext()).sendBroadcast(intent);

                                    break;
                                }
                            }
                        }
                    });
                    builder.show();
                }

            }
        };

        viewHolder.view.setOnClickListener(onClickListener);

        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                onClickListener.onClick(v);
                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return expenses.size();
    }

// ------------------------------------------->

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView expenseTitleTextView;
        public final TextView expenseAmountTextView;
        public final ViewGroup monthlyIndicator;
        public final ImageView positiveIndicator;
        public final View view;

        public ViewHolder(View v)
        {
            super(v);

            view = v;
            expenseTitleTextView = (TextView) v.findViewById(R.id.expense_title);
            expenseAmountTextView = (TextView) v.findViewById(R.id.expense_amount);
            monthlyIndicator = (ViewGroup) v.findViewById(R.id.monthly_indicator);
            positiveIndicator = (ImageView) v.findViewById(R.id.positive_indicator);
        }
    }
}