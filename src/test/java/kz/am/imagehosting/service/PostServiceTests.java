package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.dto.create.PostDto;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
public class PostServiceTests {

    @Autowired
    private PostService postService;


    @Test
    void givenEmptyPostNameWhenSavePostThenThrowRuntimeException() throws IOException {
        PostDto testPostDto = new PostDto();
        File testImageFile = new File("C:\\Users\\Alexander\\Desktop\\1\\pet\\small\\pexels-alfo-medeiros-15241238 (1).jpg");
//        testPostDto.setPostImage(new MockMultipartFile("Test Picture", Files.newInputStream(testImageFile.toPath())));
//        testPostDto.setPostName("Ugaga");
        testPostDto.setAccessId(2);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> postService.savePost(testPostDto));
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
