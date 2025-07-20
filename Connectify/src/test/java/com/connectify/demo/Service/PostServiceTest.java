package com.connectify.demo.Service;

import com.connectify.demo.Exceptions.UserNotFoundException;
import com.connectify.demo.Model.Post;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.PostRepository;
import com.connectify.demo.Repository.UserInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserInfoRepository userInfoRepository;
    @InjectMocks
    private PostService postService;

    private UserInfo sampleUser;
    private Post post;

    @BeforeEach
    void setUp() {
        sampleUser = new UserInfo();
        sampleUser.setId(1L);
        sampleUser.setName("John");
        sampleUser.setUsername("john123");
        sampleUser.setEmail("john@example.com");
        sampleUser.setPosts(new ArrayList<>());

        post = new Post();
        post.setPostId(10L);
        post.setTopic("Topic");
        post.setPostDescription("Desc");
        post.setPostUrl("url");

    }

    @Test
    void createPostTest() {
        given(userInfoRepository.findById(1L))
                .willReturn(Optional.of(sampleUser));
        given(postRepository.findById(10l))
                .willReturn(Optional.of(post));
        Optional<UserInfo> found = userInfoRepository.findById(1l);
        Post postCreated = postService.getPostById(10l);
        postCreated.setUser(found.get());
        assertThat(postCreated.getTopic()).isEqualTo("Topic");
        assertThat(postCreated.getUser().getEmail()).isEqualTo(sampleUser.getEmail());
    }

    @Test
    void getAllPostTest() {
        Post p1 = new Post();
        p1.setPostId(1L);
        Post p2 = new Post();
        p2.setPostId(2L);
        given(postRepository.findAll()).willReturn(Arrays.asList(p1, p2));

        List<Post> result = postService.getAllPost();

        assertEquals(2, result.size(), "the length of result must be 2");
    }

    @Test
    void getAllPostByUserId() {
        sampleUser.setPosts(Arrays.asList(post));
        given(userInfoRepository.findById(1L)).willReturn(Optional.of(sampleUser));

        List<Post> posts = postService.allPostByUserId(1L);

        assertThat(posts.size()).isEqualTo(1);
    }
    @Test
    @DisplayName("allPostByUserId throws when user not found")
    void whenAllPostByUserId_andNotExists_thenThrow() {
        given(userInfoRepository.findById(3L)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> postService.allPostByUserId(3L));
    }
    @Test
    @DisplayName("updatePost applies changes and returns updated post")
    void whenUpdatePost_thenApplyChanges() {
        Post updatedInfo = new Post();
        updatedInfo.setTopic("NewTopic");
        updatedInfo.setPostDescription("NewDesc");
        given(postRepository.findById(10L)).willReturn(Optional.of(post));

        Post result = postService.updatePost(updatedInfo, 10L);

        assertEquals("NewTopic", post.getTopic());
        assertEquals("NewDesc", post.getPostDescription());
        verify(postRepository).save(post);
    }
}
