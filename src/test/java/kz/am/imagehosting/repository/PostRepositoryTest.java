package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.dto.create.PostDto;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenPostWhenSaveReturnSavedPost(){
        Post testPost = new Post();
        testPost.setPostName("Test Name");

        Post savedPost = postRepository.save(testPost);
        Assertions.assertThat(savedPost).isNotNull();
        Assertions.assertThat(savedPost.getPostName()).isNotNull();
    }



    @Test
    void givenPostsWhenFindAllReturnSavedPosts() {
        Image image1 = new Image();
        image1.setImageLocation("Test Loc 1");
        imageRepository.save(image1);
        Post testPost1 = new Post();
        testPost1.setImage(image1);
        testPost1.setPostName("Test Name 1");

        Image image2 = new Image();
        image2.setImageLocation("Test Loc 2");
        imageRepository.save(image2);
        Post testPost2 = new Post();
        testPost2.setImage(image2);
        testPost2.setPostName("Test Name 2");

        postRepository.save(testPost1);
        postRepository.save(testPost2);
        List<Post> allPosts = postRepository.findAll();
        Assertions.assertThat(allPosts).isNotNull();
        Assertions.assertThat(allPosts.size()).isEqualTo(2);
    }

    @Test
    void givenPostWhenFindByIdReturnSavedPost() {
        Post testPost1 = new Post();
        testPost1.setPostName("Test Name 1");
        postRepository.save(testPost1);

        Post postFromRepo = postRepository.findById(testPost1.getId()).orElse(null);
        Assertions.assertThat(postFromRepo).isNotNull();
        Assertions.assertThat(postFromRepo.getId()).isEqualTo(testPost1.getId());
    }

    @Test
    void givenPostWhenUpdateReturnSavedPost(){
        Post testPost = new Post();
        testPost.setPostName("Test Name");

        postRepository.save(testPost);
        Post postFromRepo = postRepository.findById(testPost.getId()).orElse(null);
        postFromRepo.setPostName("New Test Name");

        Assertions.assertThat(postFromRepo).isNotNull();
        Assertions.assertThat(postFromRepo.getId()).isEqualTo(testPost.getId());
    }

    @Test
    void givenPostWhenDeleteReturnNone(){
        Post testPost = new Post();
        testPost.setPostName("Test Name");

        postRepository.save(testPost);
        postRepository.delete(testPost);
        Post postFromRepo = postRepository.findById(testPost.getId()).orElse(null);

        Assertions.assertThat(postFromRepo).isNull();
    }

    @Test
    void givenPostWhenFindPostsByCreatedByOrderByCreatedAtDescReturnSavedPost() {
        AuthUser authUserAccount = new AuthUser();
        authUserAccount.setUsername("Test User");
        authUserAccount.setPassword("Test Password");
        authUserAccount.setActive(true);
        System.out.println(authUserAccount);
        userRepository.save(authUserAccount);

        Image image1 = new Image();
        image1.setImageLocation("Test Loc 1");
        imageRepository.save(image1);
        Post testPost1 = new Post();
        testPost1.setImage(image1);
        testPost1.setPostName("Test Name 1");
        testPost1.setCreatedBy(authUserAccount);

        Image image2 = new Image();
        image2.setImageLocation("Test Loc 2");
        imageRepository.save(image2);
        Post testPost2 = new Post();
        testPost2.setImage(image2);
        testPost2.setPostName("Test Name 2");
        testPost2.setCreatedBy(authUserAccount);

        postRepository.save(testPost1);
        postRepository.save(testPost2);

        List<Post> postFromRepo = postRepository.findPostsByCreatedByOrderByCreatedAtDesc(authUserAccount);
        Assertions.assertThat(postFromRepo).isNotNull();
        Assertions.assertThat(postFromRepo.size()).isEqualTo(2);
    }

}