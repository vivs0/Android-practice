
package com.vivek.BudgetManager;

/**
 * State of checking the user status & unlocked iap
 *
 * @author Vivek Singh
 */
public enum PremiumCheckStatus
{
    INITIALIZING,

    CHECKING,

    ERROR,

    NOT_PREMIUM,

    PREMIUM
}
