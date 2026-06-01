package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.dto.create.PostDTO;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;


    @Test
    void givenEmptyPostNameWhenSavePostThenThrowRuntimeException() throws IOException {
        PostDTO testPostDTO = new PostDTO();
//        File testImageFile = new File("C:\\Users\\Alexander\\Desktop\\1\\pet\\small\\pexels-alfo-medeiros-15241238 (1).jpg");
//        testPostDTO.setPostImage(new MockMultipartFile("Test Picture", Files.newInputStream(testImageFile.toPath())));
        testPostDTO.setPostName("Ugaga");
        testPostDTO.setAccessId(2);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postService.savePost(testPostDTO));
        assertEquals("Post must have an image", exception.getMessage());
    }

    @Test
    void givenNullPostWhenDeletedThenThrowRuntimeException() {
        Post testPost = null;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postService.deletePost(testPost));
        assertEquals("Post must not be null", exception.getMessage());
    }

    @Test
    void whenGetAllPostsExpectNoExceptions() {
        assertDoesNotThrow(() -> postService.getAllPosts());
    }
}
