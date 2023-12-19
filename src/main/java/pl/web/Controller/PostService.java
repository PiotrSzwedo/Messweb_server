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
import pl.web.Entity.Post;
import pl.web.Entity.User;
import pl.web.Repository.PostRepository;
import pl.web.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

//The class responsible for acting on posts
@Controller
public class PostService {
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
        List<Post> posts = postRepository.findAll();
        if(posts.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(posts));
    }

    //____________PostMapping____________
    @PostMapping("post-id")
    //A method that finds post by id and sends result to the website
    public ResponseEntity findPostById(@RequestBody Post post) throws JsonProcessingException {
        Optional<Post> foundPost = postRepository.findById((long) post.getId());
        return ResponseEntity.ok(objectMapper.writeValueAsString(foundPost));
    }

    @PostMapping("post-title")
    //A method that finds post by title and send result to the webside
    public ResponseEntity findPostByTitle(@RequestBody Post post) throws JsonProcessingException {
        if (emptyTitle(post)) {
            Optional<Post> foundPost = postRepository.findByTitle(post.getTitle());
            return ResponseEntity.ok(objectMapper.writeValueAsString(foundPost));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("post-add")
    //A method for saveing post
    public ResponseEntity addPost(@RequestBody Post post) {
        if (emptyTitle(post) || post.getBody().isEmpty()) {
            Post post1 = postRepository.save(post);
            return ResponseEntity.ok(post1);
        } else return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("post-delete")
    //A method to deleteing post
    public ResponseEntity deletePost(@RequestBody Post post) {
        Optional<Post> deletePost = postRepository.findByTitle(post.getTitle());
        postRepository.delete(deletePost.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/owner")
    //A method that finds post's owner and sends result
    public ResponseEntity findPostOwner(@RequestBody Post post) throws JsonProcessingException {
        Optional<User> owner = userRepository.findById((long) post.getUserId());
        if (owner.isPresent()) {
            String postOwner = owner.get().getUsername() + post.getUserId();
            return ResponseEntity.ok(objectMapper.writeValueAsString(postOwner));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //____________Other____________
    //A method that has the function of checking whether the title is empty
    private boolean emptyTitle(Post post) {
        return !post.getTitle().isEmpty();
    }
}
