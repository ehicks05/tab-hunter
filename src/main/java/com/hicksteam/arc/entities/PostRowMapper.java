package com.hicksteam.arc.entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostRowMapper implements RowMapper<Post>
{
    @Override
    public Post mapRow(ResultSet row, int rowNum) throws SQLException
    {
        Post post = new Post(
                row.getLong("id"),
                row.getString("title"),
                row.getString("content"),
                row.getLong("author_id")
        );
        return post;
    }
}