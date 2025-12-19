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
        String query = "SELECT * From shopping_cart AS s " +
                " JOIN products AS p ON p.product_id = s.product_id" +
                " WHERE user_id = ?;";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    ShoppingCart cart = new ShoppingCart(userId);
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

    @Override
    public ShoppingCart add(int userId, int productId, int quantity) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) " +
                " VALUES (?, ?, ?);";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }

            return getByUserId(userId);


        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ShoppingCart update(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart " +
                " SET user_id = ?" +
                " product_id = ?" +
                " quantity = ?" +
                " WHERE user_id = ? AND product_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);
            statement.setInt(4, userId);
            statement.setInt(5, productId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }

            return getByUserId(userId);

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ShoppingCart delete(int userId, int productId) {
        String sql = "DELETE FROM shopping_cart " +
                " WHERE user_id = ? AND product_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, productId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }

            return getByUserId(userId);

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    //clears entire cart
    @Override
    public ShoppingCart clear(int userId)
    {

        String sql = "DELETE FROM shopping_cart " +
                " WHERE user_id = ?; ";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                return null;
            }

            return getByUserId(userId);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}

