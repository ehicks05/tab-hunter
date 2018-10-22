package com.hicksteam.tab;

import org.springframework.jdbc.core.JdbcTemplate;

public class DAO
{
    private static JdbcTemplate m_jdbcTemplate;

    public static JdbcTemplate getJdbcTemplate()
    {
        return m_jdbcTemplate;
    }

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        m_jdbcTemplate = jdbcTemplate;
    }
}
