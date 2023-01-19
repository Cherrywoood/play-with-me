package com.example.playwithmeauth;

import com.example.playwithmeauth.exception.Error;
import com.example.playwithmeauth.model.Auth;
import com.example.playwithmeauth.model.User;
import com.example.playwithmeauth.repository.UserDao;
import com.example.playwithmeauth.token.IAMTokenUtil;
import com.example.playwithmeauth.token.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.sql.*;
import java.util.UUID;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private Connection dbcon;

    @Override
    public void init() throws ServletException {

        String DB_HOST = System.getenv("host");
        String DB_PORT = System.getenv("port");
        String DB_NAME = System.getenv("database");
        String DB_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?ssl=true&sslmode=require";
        String DB_USER = System.getenv("user");
        String DB_PASS = IAMTokenUtil.getAccessToken("https://functions.yandexcloud.net/d4e8gq2dml5spbjd30g7");

        try {
            Class.forName("org.postgresql.Driver");

            dbcon = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Class not found Error");
        } catch (SQLException e) {
            throw new RuntimeException("Connection exception");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        User user = mapToUser(req, resp);

        if (user == null) {
            return;
        }

        if (UserDao.existsUserByUsername(dbcon, user.getUsername())) {
            sendConflictError(resp, "user with this username exists");
            return;
        }

        if (UserDao.save(dbcon, user) > 0) {
            System.out.println("user insert");
        } else System.err.println("user insert failed");

        Auth auth = new Auth(user, JwtTokenUtil.createToken(user.getId(), user.getUsername()));
        resp.setStatus(201);
        resp.getWriter().println(new ObjectMapper().writeValueAsString(auth));
        resp.getWriter().close();
    }

    private User mapToUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new ObjectMapper().readValue(req.getReader(), User.class);

        if (user.getUsername() == null || user.getUsername().length() == 0) {
            sendBadRequestError(resp, "username cannot be null or empty");
            return null;
        } else if (user.getUsername().length() < 5 || user.getUsername().length() > 15) {
            sendBadRequestError(resp, "username must be between 5 and 15 characters");
            return null;
        } else if (user.getPassword() == null || user.getPassword().length() == 0) {
            sendBadRequestError(resp, "password cannot be null or empty");
            return null;
        } else if (user.getPassword().length() < 5) {
            sendBadRequestError(resp, "password must be more than 5 characters");
            return null;
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setId(UUID.randomUUID());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }


    private void sendConflictError(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(409);
        Error error = new Error(409, "Conflict", message);
        resp.getWriter().println(new ObjectMapper().writeValueAsString(error));
        resp.getWriter().close();
    }

    private void sendBadRequestError(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(400);
        Error error = new Error(400, "Bad Request", message);
        resp.getWriter().println(new ObjectMapper().writeValueAsString(error));
        resp.getWriter().close();
    }

    @Override
    public void destroy() {
        try {
            dbcon.close();
        } catch (SQLException e) {
            throw new RuntimeException("Connection close exception");
        }
    }
}
