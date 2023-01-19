package com.example.playwithmelogin.repository;

import com.example.playwithmelogin.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDao {

    public static User findUserByUsername(Connection connection, String username) {
        try {
            PreparedStatement find = connection.prepareStatement("select * from users where username = ?");
            find.setString(1, username);
            ResultSet findResult = find.executeQuery();
            if (findResult.next()) {
                User user = new User();
                user.setId(findResult.getObject("id", UUID.class));
                user.setUsername(findResult.getString("username"));
                user.setPassword(findResult.getString("password"));
                findResult.close();
                find.close();
                return user;
            } else{
                findResult.close();
                find.close();
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Sql exception");
        }
    }
}
