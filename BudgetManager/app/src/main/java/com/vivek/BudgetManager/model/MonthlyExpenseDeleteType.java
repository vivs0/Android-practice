
package com.vivek.BudgetManager.model;

import android.support.annotation.Nullable;

/**
 * An enum that reference different kind of deletion for a monthly expense
 *
 * @author Vivek Singh
 */
public enum MonthlyExpenseDeleteType
{
    /**
     * Delete all from a date
     */
    FROM(0),

    /**
     * Delete all before a date
     */
    TO(1),

    /**
     * Delete all
     */
    ALL(2),

    /**
     * Delete this expense occurrence only
     */
    ONE(3);

// ------------------------------------->

    /**
     * Integer value (for serialization)
     */
    private final int value;

    /**
     * Constructor private
     *
     * @param value
     */
    MonthlyExpenseDeleteType(int value)
    {
        this.value = value;
    }

// ------------------------------------->

    /**
     * Get the value for serialization
     *
     * @return
     */
    public int getValue()
    {
        return value;
    }

// ------------------------------------->

    /**
     * Retrieve the enum for the given value
     *
     * @param value
     * @return
     */
    @Nullable
    public static MonthlyExpenseDeleteType fromValue(int value)
    {
        for (MonthlyExpenseDeleteType type : values())
        {
            if (value == type.getValue())
            {
                return type;
            }
        }

        return null;
    }
}