//package be.ucll.da.userservice.adapters.rest.incoming;
//
//import be.ucll.da.userservice.api.UserApi;
//import be.ucll.da.userservice.api.UserApiDelegate;
//import be.ucll.da.userservice.api.model.ApiUser;
//import be.ucll.da.userservice.api.model.ApiUsers;
//import be.ucll.da.userservice.domain.User;
//import be.ucll.da.userservice.domain.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserController implements UserApiDelegate {
//
//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public ResponseEntity<Void> createUser(ApiUser user) {
//        userService.createUser(user);
//        return ResponseEntity.ok().build();
//    }
//
//    @Override
//    public ResponseEntity<Void> addFriend(Integer userId, Integer friendId) {
//        userService.addFriend(userId, friendId);
//        return ResponseEntity.ok().build();
//    }
//
//    @Override
//    public ResponseEntity<ApiUsers> getUsers() {
//        ApiUsers users = new ApiUsers();
//        users.addAll(
//            userService.getUsers().stream()
//                .map(this::toDto)
//                .toList()
//        );
//        return ResponseEntity.ok(users);
//    }
//
//    private ApiUser toDto(User user) {
//        return new ApiUser()
//                .id(user.getId())
//                .friends(user.getFriends())
//                .email(user.getEmail())
//                .lastName(user.getLastName())
//                .firstName(user.getFirstName());
//    }
//}
