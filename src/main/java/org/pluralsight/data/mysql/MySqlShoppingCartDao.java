package org.pluralsight.data.mysql;

import org.pluralsight.data.ShoppingCartDao;

import org.pluralsight.models.ShoppingCart;
import org.pluralsight.models.ShoppingCartItem;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCartDao(DataSource dataSource)
    {
        super(dataSource);
    }

    //Shopping Cart Methods
    @Override
    public ShoppingCart getByUserId(int userId) {
        String query = "SELECT * From shopping_cart AS s" +
                " JOIN products AS p ON p.product_id = s.product_id" +
                " WHERE user_id = ?;";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    ShoppingCart cart = new ShoppingCart();
                    ShoppingCartItem item = new ShoppingCartItem(MySqlProductDao.mapRow(results),results.getInt("quantity"));
                    cart.getItems().put(results.getInt("quantity"),item );

                    return cart;
                    
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error pulling up your shopping cart.", e);
        }

        return null;
    }


}

