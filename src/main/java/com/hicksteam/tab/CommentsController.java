package com.hicksteam.tab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hicksteam.tab.entities.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.List;

@RestController
public class CommentsController
{
    private static final Logger log = LoggerFactory.getLogger(CommentsController.class);

    @GET
    @RequestMapping("/api/comments")
    public String getAllComments()
    {
        String results = "";
        List<Comment> comments = Comment.getAllComments();
        ObjectMapper objectMapper = JSONutil.objectMapper;
        try
        {
            results = objectMapper.writeValueAsString(comments);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return results;
    }

    public static class AjaxComment
    {
        private long postId;
        private long parentCommentId;
        private String text;

        public long getPostId()
        {
            return postId;
        }

        public void setPostId(long postId)
        {
            this.postId = postId;
        }

        public long getParentCommentId()
        {
            return parentCommentId;
        }

        public void setParentCommentId(long parentCommentId)
        {
            this.parentCommentId = parentCommentId;
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/comments")
    public Comment create(@RequestBody AjaxComment ajaxComment)
    {
        Comment comment = new Comment();
        comment.setPostId(ajaxComment.getPostId());
        comment.setParentCommentId(ajaxComment.getParentCommentId());
        comment.setContent(ajaxComment.getText());
        Comment.createComment(comment);

        return comment;
    }

    @DELETE
    @RequestMapping("/api/comments/{id}")
    public String deleteComment(@PathVariable Long id)
    {
        int rowsUpdated = Comment.deleteComment(id);

        return JSONutil.writeValueAsString(new DeleteCommentResults(rowsUpdated + " rows updated"));
    }

    public static class DeleteCommentResults
    {
        String body = "";

        public DeleteCommentResults(String body)
        {
            this.body = body;
        }

        public String getBody()
        {
            return body;
        }

        public void setBody(String body)
        {
            this.body = body;
        }
    }

    @POST
    @RequestMapping("/api/comments/post")
    public void createComment(
            @RequestParam(value = "parentId") String parentId,
            @RequestParam(value = "content", defaultValue = "") String content
    )
    {
        Comment parent = Comment.getCommentById(Long.valueOf(parentId));

        if (parent != null)
        {
            Comment comment = new Comment();
            comment.setParentCommentId(Long.valueOf(parentId));
            comment.setContent(content);
            comment.setAuthorId(0);
            comment.setPostId(parent.getPostId());

            Comment.createComment(comment);
        }
    }
}
