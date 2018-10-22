package com.hicksteam.tab.entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment>
{
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Comment comment = new Comment(
                rs.getLong("id"),
                rs.getLong("post_id"),
                rs.getLong("parent_comment_id"),
                rs.getLong("author_id"),
                rs.getString("content")
        );
        return comment;
    }
}
