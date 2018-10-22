package com.hicksteam.tab.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.hicksteam.tab.DAO;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post
{
    private long id;
    private String title;
    private String content;
    private long authorId;

    public Post(long id, String title, String content, long authorId)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    public Post()
    {
    }

    public String toString()
    {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                '}';
    }

    //----------DATA ACCESS
    public static long createPost(Post post)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", post.getTitle());
        parameters.put("content", post.getContent());
        parameters.put("author_id", post.getAuthorId());
        Number number = new SimpleJdbcInsert(DAO.getJdbcTemplate()).usingGeneratedKeyColumns("id").withSchemaName("arc").withTableName("posts").executeAndReturnKey(parameters);
        return number.longValue();
    }

    public static List<Post> getAllPosts()
    {
        String query = "select * from posts";
        return DAO.getJdbcTemplate()
                .query(query, new Object[]{}, new PostRowMapper());
    }

    public static Post getById(long id)
    {
        String query = "select * from posts where id=?";
        return DAO.getJdbcTemplate()
                .queryForObject(query, new Object[]{id}, new PostRowMapper());
    }

    public static void mapJSONtoObject(JsonNode json)
    {
        JsonNode postData = json.findValue("data");
        JsonNode title = postData.findValue("title");
        JsonNode selfText = postData.findValue("selftext");

        //popular demo users from the reddit posts
        Long usernameId = null;
        JsonNode authorName = postData.findValue("author");
        String userName = authorName.textValue();
        boolean userExists = User.userExists(userName);
        if (!userExists)
            usernameId = User.createUser(new User(userName, "1234", true));
        else
            usernameId = User.getUserByUsername(userName).getUserId();

        Post post = new Post();
        post.setTitle(title.asText());
        post.setContent(selfText.textValue());
        post.setAuthorId(usernameId);

        long postId = Post.createPost(post);
    }

    public String getAuthor()
    {
        User user = User.getById(this.authorId);
        return user != null ? user.getUsername() : "";
    }

    public int getComments()
    {
        return Comment.getByPostId(this.id).size();
    }

    public List<Comment> getCommentTree()
    {
        return Comment.getRootCommentsByPostId(this.id);
    }


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public long getAuthorId()
    {
        return authorId;
    }

    public void setAuthorId(long authorId)
    {
        this.authorId = authorId;
    }
}
