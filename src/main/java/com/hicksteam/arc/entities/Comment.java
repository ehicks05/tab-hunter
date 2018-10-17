package com.hicksteam.arc.entities;

import com.hicksteam.arc.DAO;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Comment
{
    private long id;
    private long postId;
    private Long parentCommentId;
    private long authorId;
    private String content = "";

    public Comment(long id, long postId, Long parentCommentId, long authorId, String content)
    {
        this.id = id;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.authorId = authorId;
        this.content = content;
    }

    public Comment()
    {
    }

    @Override
    public String toString()
    {
        return "Comment for post: " + postId + " by: " + authorId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comments = (Comment) o;
        return id == comments.id;
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(id);
    }

    // ------DATA ACCESS METHODS
    public static long createComment(Comment comment)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("parent_comment_id", comment.getParentCommentId());
        parameters.put("author_id", comment.getAuthorId());
        parameters.put("content", comment.getContent());
        parameters.put("post_id", comment.getPostId());
        Number number = new SimpleJdbcInsert(DAO.getJdbcTemplate())
                .usingGeneratedKeyColumns("id")
                .withSchemaName("arc")
                .withTableName("comments")
                .executeAndReturnKey(parameters);
        return number.longValue();
    }

    public static int deleteComment(long id)
    {
        String sql = "update comments set content='DELETED' where id=?";
        return DAO.getJdbcTemplate().update(sql, id);
    }

    public static List<Comment> getAllComments()
    {
        String query = "select * from comments";
        List<Comment> comments = DAO.getJdbcTemplate()
                .query(query, new Object[]{}, new CommentRowMapper());

        return comments;
    }

    public static List<Comment> getByPostId(long postId)
    {
        return DAO.getJdbcTemplate().query("select * from comments where post_id=?",
                new Object[]{postId}, new CommentRowMapper());
    }

    public static Comment getCommentById(long commentId)
    {
        String query = "select * from Comments where id=?";
        return DAO.getJdbcTemplate()
                .queryForObject(query, new Object[]{commentId}, new CommentRowMapper());
    }

    public static List<Comment> getRootCommentsByPostId(long postId)
    {
        return DAO.getJdbcTemplate().query("select * from comments where post_id=? and parent_comment_id=0",
                new Object[]{postId}, new CommentRowMapper());
    }

    public List<Comment> getChildren()
    {
        return DAO.getJdbcTemplate().query("select * from comments where post_id=? and parent_comment_id=?",
                new Object[]{postId, id}, new CommentRowMapper());
    }

    public String getAuthor()
    {
        User user = User.getById(this.authorId);
        return user != null ? user.getUsername() : "";
    }

    //----------GETTERS & SETTERS
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getPostId()
    {
        return postId;
    }

    public void setPostId(long postId)
    {
        this.postId = postId;
    }

    public Long getParentCommentId()
    {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId)
    {
        this.parentCommentId = parentCommentId;
    }

    public long getAuthorId()
    {
        return authorId;
    }

    public void setAuthorId(long authorId)
    {
        this.authorId = authorId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
