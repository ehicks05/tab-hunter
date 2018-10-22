package com.hicksteam.tab.entities;

import com.hicksteam.tab.DAO;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.Map;

public class UserRole
{
    private long userRoleId;
    private String username;
    private String role;

    public UserRole(String username, String role)
    {
        this.username = username;
        this.role = role;
    }

    public UserRole()
    {

    }

    @Override
    public String toString()
    {
        return "UserRole{" +
                "userRoleId=" + userRoleId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    //--------Data Access
    public static long createUserRole(UserRole userRole)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_role_id", userRole.getUserRoleId());
        parameters.put("username", userRole.getUsername());
        parameters.put("role", userRole.getRole());
        Number number = new SimpleJdbcInsert(DAO.getJdbcTemplate()).usingGeneratedKeyColumns("user_role_id").withSchemaName("tab").withTableName("user_roles").executeAndReturnKey(parameters);
        return number.longValue();
    }

    public long getUserRoleId()
    {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId)
    {
        this.userRoleId = userRoleId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }
}
