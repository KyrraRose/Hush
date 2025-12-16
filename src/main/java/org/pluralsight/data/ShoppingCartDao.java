package org.pluralsight.data;

import org.pluralsight.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);

    // add additional method signatures here
}
