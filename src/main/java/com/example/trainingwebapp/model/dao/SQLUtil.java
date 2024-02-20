package com.example.trainingwebapp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLUtil implements AutoCloseable {
    Connection connection;
    Statement statement;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    @Override
    public void close() {
        try {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
