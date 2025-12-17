package org.pluralsight.data;

import org.pluralsight.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCart add(int userId, int productId, int quantity); //CREATE
    ShoppingCart update(int userId, int productId, int quantity);
    ShoppingCart delete(int userId, int productId);
    ShoppingCart clear(int userId);

}
