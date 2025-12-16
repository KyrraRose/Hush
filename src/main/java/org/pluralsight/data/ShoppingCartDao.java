package org.pluralsight.data;

import org.pluralsight.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCart add(int userId, int productId, int quantity); //CREATE
    void update(int userId, int productId, int quantity);
    void delete(int userId, int productId);

}
