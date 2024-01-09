package pl.web.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.web.Entity.PostEntity;
import pl.web.Entity.User;
import pl.web.Repository.PostRepository;
import pl.web.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

//The class responsible for acting on posts
@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    //____________GetMapping____________
    @GetMapping("/posts")
    //A method that sends json from the list of posts to the website
    public ResponseEntity Posts() throws JsonProcessingException {
        List<PostEntity> postEntities = postRepository.findAll();
        if(postEntities.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        //objectMapper.writeValueAsString(postEntities)
        return ResponseEntity.ok().build();
    }

    //____________PostMapping____________
    @PostMapping("postEntity-id")
    //A method that finds postEntity by id and sends result to the website
    public ResponseEntity findPostById(@RequestBody PostEntity postEntity) throws JsonProcessingException {
        Optional<PostEntity> foundPost = postRepository.findById((long) postEntity.getId());
        return ResponseEntity.ok(objectMapper.writeValueAsString(foundPost));
    }

    @PostMapping("post-title")
    //A method that finds postEntity by title and send result to the webside
    public ResponseEntity findPostByTitle(@RequestBody PostEntity postEntity) throws JsonProcessingException {
        if (emptyTitle(postEntity)) {
            Optional<PostEntity> foundPost = postRepository.findByTitle(postEntity.getTitle());
            return ResponseEntity.ok(objectMapper.writeValueAsString(foundPost));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("post-add")
    //A method for saveing postEntity
    public ResponseEntity addPost(@RequestBody PostEntity postEntity) {
        if (emptyTitle(postEntity) || postEntity.getBody().isEmpty()) {
            PostEntity postEntity1 = postRepository.save(postEntity);
            return ResponseEntity.ok(postEntity1);
        } else return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("post-delete")
    //A method to deleteing postEntity
    public ResponseEntity deletePost(@RequestBody PostEntity postEntity) {
        Optional<PostEntity> deletePost = postRepository.findByTitle(postEntity.getTitle());
        postRepository.delete(deletePost.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post-owner")
    //A method that finds postEntity's owner and sends result
    public ResponseEntity findPostOwner(@RequestBody PostEntity postEntity) throws JsonProcessingException {
        Optional<User> owner = userRepository.findById((long) postEntity.getUser_id());
        if (owner.isPresent()) {
            String postOwner = owner.get().getUsername() + postEntity.getUser_id();
            return ResponseEntity.ok(objectMapper.writeValueAsString(postOwner));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //____________Other____________
    //A method that has the function of checking whether the title is empty
    private boolean emptyTitle(PostEntity postEntity) {
        return !postEntity.getTitle().isEmpty();
    }
}
