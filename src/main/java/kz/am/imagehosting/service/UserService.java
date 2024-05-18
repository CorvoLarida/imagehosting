package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.repository.PostCollectionRepository;
import kz.am.imagehosting.repository.PostRepository;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCollectionRepository pcRepository;

    @Autowired
    public UserService(UserRepository userRepository, PostRepository postRepository, PostCollectionRepository pcRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.pcRepository = pcRepository;
    }

    public List<AuthUser> findAllUsers(){
        return userRepository.findAll();
    }

}
