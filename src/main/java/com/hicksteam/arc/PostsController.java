package com.hicksteam.arc;

import com.hicksteam.arc.entities.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostsController
{
    private static final Logger log = LoggerFactory.getLogger(PostsController.class);

    @RequestMapping(method = RequestMethod.GET, path = "/api/posts")
    public String getAllPosts()
    {
        return JSONutil.writeValueAsString(Post.getAllPosts());
    }


    @RequestMapping(method = RequestMethod.POST, path = "/api/posts")
    public void create(@RequestBody String requestBody)
    {
        Post post = new Post();
//        post.set
    }

    @RequestMapping("/api/posts/{id}")
    public String deletePost(@PathVariable long id)
    {
        // todo: implement
        return JSONutil.writeValueAsString(Post.getById(id));
    }
}