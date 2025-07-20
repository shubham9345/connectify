package com.connectify.demo.Controller;

import com.connectify.demo.Model.Post;
import com.connectify.demo.Service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("POST /api/add-post/{userId} should return created post")
    void whenAddPost_thenReturnPost() throws Exception {
        Post input = new Post();
        input.setTopic("Topic");
        input.setPostDescription("Desc");

        Post returned = new Post();
        returned.setPostId(1L);
        returned.setTopic("Topic");
        returned.setPostDescription("Desc");

        when(postService.addPost(any(Post.class), eq(1L))).thenReturn(returned);

        mockMvc.perform(post("/api/add-post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.topic").value("Topic"))
                .andExpect(jsonPath("$.postDescription").value("Desc"));
    }

    @Test
    @DisplayName("GET /api/all-post should return list of posts")
    void whenGetAllPost_thenReturnList() throws Exception {
        Post p1 = new Post();
        p1.setPostId(1L);
        Post p2 = new Post();
        p2.setPostId(2L);
        List<Post> posts = Arrays.asList(p1, p2);

        when(postService.getAllPost()).thenReturn(posts);

        mockMvc.perform(get("/api/all-post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(1))
                .andExpect(jsonPath("$[1].postId").value(2));
    }

    @Test
    @DisplayName("GET /api/post/{postId} should return a post")
    void whenGetPostById_thenReturnPost() throws Exception {
        Post p = new Post();
        p.setPostId(5L);
        when(postService.getPostById(5L)).thenReturn(p);

        mockMvc.perform(get("/api/post/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(5));
    }

    @Test
    @DisplayName("GET /api/all-post/{userId} returns user's posts")
    void whenGetPostByUserId_thenReturnList() throws Exception {
        Post p = new Post();
        p.setPostId(7L);
        when(postService.allPostByUserId(10L)).thenReturn(Collections.singletonList(p));

        mockMvc.perform(get("/api/all-post/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(7));
    }

    @Test
    @DisplayName("PATCH /api/update-post/{postId} returns updated post")
    void whenUpdatePost_thenReturnUpdated() throws Exception {
        Post input = new Post();
        input.setTopic("New");
        input.setPostDescription("desc");

        Post updated = new Post();
        updated.setPostId(8L);
        updated.setTopic("updatedTopic");
        updated.setPostDescription("updatedDesc");

        when(postService.updatePost(any(Post.class), eq(8L))).thenReturn(updated);

        mockMvc.perform(patch("/api/update-post/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(8))
                .andExpect(jsonPath("$.topic").value("updatedTopic"))
                .andExpect(jsonPath("$.postDescription").value("updatedDesc"));
    }

    @Test
    @DisplayName("DELETE /api/delete-post/{userId}/{postId} returns deletion status")
    void whenRemovePostByUserId_thenReturnStatus() throws Exception {
        when(postService.removePostByUserId(1L, 2L)).thenReturn(1);

        mockMvc.perform(delete("/api/delete-post/1/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("post is deleted with Id - 2"));

        when(postService.removePostByUserId(1L, 2L)).thenReturn(0);

        mockMvc.perform(delete("/api/delete-post/1/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("post is not deleted with Id - 2"));
    }
}
