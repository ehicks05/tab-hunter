package com.hicksteam.tab.entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User>
{
    @Override
    public User mapRow(ResultSet row, int rowNumb) throws SQLException
    {
        User user = new User();
        user.setUserId(row.getLong("user_id"));
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        user.setEnabled(row.getBoolean("enabled"));

        return user;
    }
}
