
package com.vivek.BudgetManager;

/**
 * Listener for in-app purchase buying flow
 *
 * @author Vivek Singh
 */
public interface PremiumPurchaseListener
{
    /**
     * Called when the user cancel the purchase
     */
    void onUserCancelled();

    /**
     * Called when an error occurred during the iab flow
     *
     * @param error the error description
     */
    void onPurchaseError(String error);

    /**
     * Called on success
     */
    void onPurchaseSuccess();
}
