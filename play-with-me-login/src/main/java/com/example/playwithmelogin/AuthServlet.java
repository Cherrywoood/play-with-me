package com.example.playwithmelogin;

import com.example.playwithmelogin.exception.Error;
import com.example.playwithmelogin.model.Auth;
import com.example.playwithmelogin.model.User;
import com.example.playwithmelogin.repository.UserDao;
import com.example.playwithmelogin.token.IAMTokenUtil;
import com.example.playwithmelogin.token.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {
    private Connection dbcon;

    @Override
    public void init() throws ServletException {
        System.out.println("try to connect");

        String DB_HOST = System.getenv("host");
        String DB_PORT = System.getenv("port");
        String DB_NAME = System.getenv("database");
        String DB_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?ssl=true&sslmode=require";
        String DB_USER = System.getenv("user");
        String DB_PASS = IAMTokenUtil.getAccessToken("https://functions.yandexcloud.net/d4e8gq2dml5spbjd30g7");
        /*String DB_URL = "jdbc:postgresql://localhost:5434/play_with_me";
        String DB_USER = "postgres";
        String DB_PASS = "12345";*/

        try {
            Class.forName("org.postgresql.Driver");

            dbcon = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Class not found Error");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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

        User findUser = UserDao.findUserByUsername(dbcon, user.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (findUser == null || !encoder.matches(user.getPassword(), findUser.getPassword())) {
            sendUnauthorizedError(resp, "invalid user credentials");
            return;
        }

        Auth auth = new Auth(findUser, JwtTokenUtil.createToken(findUser.getId(), findUser.getUsername()));
        resp.setStatus(200);
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

        return user;
    }


    private void sendUnauthorizedError(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(401);
        Error error = new Error(401, "Unauthorized", message);
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
