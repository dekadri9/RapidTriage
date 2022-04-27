package com.example.RapidTriage.Controllers;

import com.example.RapidTriage.Exceptions.UserNotFoundException;
import com.example.RapidTriage.Input.UserInput;
import com.example.RapidTriage.Output.UserOutput;
import com.example.RapidTriage.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserOutput> getAllUsers(){
        return userService.getUsers();
    }

    @GetMapping(path = "/token/{userId}")
    public Map<String, String> getToken(@PathVariable(name = "userId", required = true) Long id) throws LoginException {
        return userService.getToken(id);
    }

    @PostMapping
    public UserOutput postUser(@RequestBody UserInput userInput){
        return userService.postUser(userInput);
    }

    @PostMapping(path = "/login")
    @ResponseBody
    public Map<String, String> userLogin(@RequestBody UserInput userInput){
        return userService.userLogin(userInput);
    }

    @PostMapping(path = "{userId}/logout")
    @ResponseStatus(value = HttpStatus.OK, reason = "Logout succesful")
    public void userLogout(@PathVariable(name = "userId", required = true) Long userId,
                           @RequestHeader(value = "auth", required = true) String token) throws LoginException {
        userService.userLogout(userId,token);
    }

    @GetMapping(path = "{userId}")
    public UserOutput getUserById(@PathVariable(name = "userId", required = true) Long id){
        return userService.getUser(id);
    }

    @GetMapping(path = "/username/{username}")
    public UserOutput getUserByUsername(@PathVariable("username") String username) throws UserNotFoundException {
        return userService.getUserByUsername(username);
    }

    @GetMapping(path = "/email/{email}")
    public UserOutput getUserByEmail(@PathVariable("email") String email) throws UserNotFoundException {
        return userService.getUserByEmail(email);
    }

    @PutMapping(path = "{userId}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public UserOutput modifyUser(@PathVariable(name = "userId", required = true) Long id, @RequestBody UserInput userInput,
                      @RequestHeader(value = "auth", required = true) String token) throws LoginException {
        return userService.modifyUser(id, userInput, token);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable(name = "userId", required = true) Long id, @RequestHeader(value = "auth", required = true)
                           String token){
        userService.deleteUser(id, token);
    }

    @PostMapping("/forgotPassword")
    public Void forgotPassword(@RequestBody (required = true) UserInput userInput){
        return userService.forgotPassword(userInput);
    }

    /*@PostMapping("/deactivateUser/{userId}")
    @ResponseStatus(value = HttpStatus.OK, reason = "The user has been deactivated")
    public Void deactivateUser(@PathVariable(name = "userId", required = true) Long userId, @RequestHeader(value = "auth", required = true) String token) throws Exception{
        return userService.deactivateUser(userId, token);
    }*/






}
