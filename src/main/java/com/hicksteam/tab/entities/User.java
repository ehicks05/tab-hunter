package com.hicksteam.tab.entities;

import com.hicksteam.tab.DAO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User
{
    private long userId;
    private String username;
    private String password;
    private boolean enabled;

    public User(String username, String password, boolean enabled)
    {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User()
    {

    }

    @Override
    public String toString()
    {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    //--------Data Access
    public static long createUser(User user)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", user.getUserId());
        parameters.put("username", user.getUsername());
        parameters.put("password", user.getPassword());
        parameters.put("enabled", user.isEnabled());
        Number number = new SimpleJdbcInsert(DAO.getJdbcTemplate()).usingGeneratedKeyColumns("user_id").withSchemaName("tab").withTableName("users").executeAndReturnKey(parameters);
        return number.longValue();
    }

    public static boolean userExists(String username)
    {
        return getUserByUsername(username) != null;
    }

    public static User getById(long id)
    {
        try
        {
            return DAO.getJdbcTemplate().queryForObject("select * from users where user_id=?",
                    new Object[]{id}, new UserRowMapper());
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }

    public static User getUserByUsername(String username)
    {
        List<User> users = DAO.getJdbcTemplate().query("select * from users where username=?", new Object[]{username}, new UserRowMapper());
        if (users.size() > 0)
            return users.get(0);

        return null;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
