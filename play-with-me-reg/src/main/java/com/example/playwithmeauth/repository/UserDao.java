package com.example.playwithmeauth.repository;

import com.example.playwithmeauth.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {

    public static boolean existsUserByUsername(Connection connection, String username) {
        try {
            PreparedStatement find = connection.prepareStatement("select 1 from users where username = ?");
            find.setString(1, username);
            boolean findResult = find.executeQuery().next();
            find.close();
            return findResult;
        } catch (SQLException e) {
            throw new RuntimeException("Sql exception");
        }
    }

    public static int save(Connection connection, User user) {
        try {
            PreparedStatement insert = connection.prepareStatement("insert into users values(?,?,?)");
            insert.setObject(1, user.getId());
            insert.setString(2, user.getUsername());
            insert.setString(3, user.getPassword());
            int insertResult = insert.executeUpdate();
            insert.close();
            return insertResult;
        } catch (SQLException e) {
            throw new RuntimeException("Sql exception");
        }

    }
}
