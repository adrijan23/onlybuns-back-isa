package team5.onlybuns.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional; // <—
import team5.onlybuns.model.User;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.impl.UserServiceImpl;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class FollowServiceIT extends ITBase {

    @Autowired UserServiceImpl userService;
    @Autowired UserRepository userRepository;

    @Test
    @Transactional
    void followUser_happyPath_persistsRelationAndCount() {
        User follower = new User();
        follower.setUsername("f1");
        follower.setEmail("f1@ex.com");
        follower.setPassword("enc");
        follower.setFollowing(new HashSet<>());
        follower = userRepository.save(follower);

        User following = new User();
        following.setUsername("f2");
        following.setEmail("f2@ex.com");
        following.setPassword("enc");
        following.setFollowersCount(0L);
        following = userRepository.save(following);

        userService.followUser(follower.getId(), following.getId());

        // re-fetch unutar iste transakcije → lazy kolekcija je inicijalizabilna
        User followerDb = userRepository.findById(follower.getId()).orElseThrow();
        User followingDb = userRepository.findById(following.getId()).orElseThrow();

        assertThat(followerDb.getFollowing())
                .extracting(User::getId)
                .contains(followingDb.getId());
        assertThat(followingDb.getFollowersCount()).isEqualTo(1);
    }
}
