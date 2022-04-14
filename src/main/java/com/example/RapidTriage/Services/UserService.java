package com.example.RapidTriage.Services;

import com.example.RapidTriage.Exceptions.AlreadyLoginException;
import com.example.RapidTriage.Exceptions.IncorrectTokenException;
import com.example.RapidTriage.Exceptions.UserNotFoundException;
import com.example.RapidTriage.Input.UserInput;
import com.example.RapidTriage.Models.User;
import com.example.RapidTriage.Output.UserOutput;
import com.example.RapidTriage.Repositories.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public UserService(UserRepository userRepository, JavaMailSender javaMailSender){
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    /*Gets all users in system*/
    public List<UserOutput> getUsers(){
        List<UserOutput> users = new ArrayList<>();

        for (User u : userRepository.findAll()){
            UserOutput userOutput = new UserOutput(u);
            users.add(userOutput);
        }
        return users;
    }

    /*Returns an User*/
    public UserOutput getUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists) throw new IllegalStateException("The user with id " + userId + " does not exist");
        User u = userRepository.getById(userId);
        return new UserOutput(u);
    }

    /*Register new user in system ensuring integrity rules*/
    public UserOutput postUser(UserInput userInput){
        if (userInput.getEmail() == null) throw new HttpMessageConversionException("The email field cannot be empty");
        else if (userRepository.findUserByEmail(userInput.getEmail()).isPresent())
            throw new HttpMessageConversionException("This email already exists");
        //else if (userInput.getPassword().equals("deactivated"))
        // throw new HttpMessageConversionException("This password isn't safe enough");
        else if (userInput.getName() == null) throw new HttpMessageConversionException("The field name can't be empty");
        else if (userInput.getUsername() == null) throw new HttpMessageConversionException("The field username can't be empty");
        else if (userRepository.findUserByUsername(userInput.getUsername()).isPresent())
            throw new HttpMessageConversionException("This username is already in use");
        else if (userInput.getPassword() == null) throw new HttpMessageConversionException("The password field can't be empty");
        else if (userInput.getBirthDate() == null) throw new HttpMessageConversionException("The birthdate can't be empty");
        else{
            User user = new User(userInput.getEmail(),userInput.getName(),userInput.getUsername(),userInput.getPassword(),userInput.getBirthDate());
            user = userRepository.save(user);
            return new UserOutput(user);
        }
    }

    //Modify user fields if he/she is allowed by token
    @Transactional
    public UserOutput modifyUser(Long id, UserInput userInput, String token) throws LoginException, UserNotFoundException, IncorrectTokenException {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);
        User user = userRepository.getById(id);
        if(user.getToken() != null){
            if (user.getToken().equals(token)){
                if (user.getName() != null) user.setName(userInput.getName());
                if (user.getEmail() != null && !userRepository.findUserByEmail(userInput.getEmail()).isPresent()){
                    user.setEmail(userInput.getEmail());
                }
                if (user.getUsername() != null) user.setUsername(userInput.getUsername());
                userRepository.save(user);
                return new UserOutput(user);
            }
            else throw new IncorrectTokenException();
        }
        else throw new LoginException();
    }

    /*Delete user from system*/
    public void deleteUser(Long userId, String token) throws IncorrectTokenException, UserNotFoundException{

        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        User user = userRepository.getById(userId);

        if (user.getToken().equals(token)) userRepository.delete(user);
        else throw new IncorrectTokenException();
    }

    /*Cambiar mensaje de recuperacion, posibilidad de generar una nueva codificada y poder cambiarla*/
    public Void forgotPassword(UserInput userDetails) throws UserNotFoundException {

        String email = userDetails.getEmail();

        if (!userRepository.findUserByEmail(email).isPresent()) throw new UserNotFoundException(email);
        User user = userRepository.getUserByEmail(email);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("rapidTriageApp@gmail.com");
        mail.setSubject("Your RapidTriage password");
        mail.setText("Hello " + user.getName() + ", " +
                "\n Somebody requested the password for the RapidTriage account associated with " +
                user.getEmail() + ". \n No changes have been made to your account. \n Here you have your BookSpace password: "
                + user.getPassword() + " \n If you did not request a new password," +
                " please let us know immediately by replying to this email. \n Yours, \n The RapidTriage team.");

        javaMailSender.send(mail);
        return null;
    }

    /*Deactivate user account (optional)
    public Void deactivateUser(Long userId, String token) {

        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        User user = userRepository.getById(userId);

        logout(userId, token);

        user.setPassword("deactivated");
        user.setUsername("User" + user.getId());
        user.setEmail("user." + user.getId() + "@bookspace.com");
        user.setName("User " + user.getId());
        userRepository.save(user);

        return null;
    }*/

    public Map<String, String> userLogin(UserInput userInput){
        if (userInput.getEmail() == null) throw new HttpMessageConversionException("The mail can't be null");
        if (userInput.getPassword() == null) throw new HttpMessageConversionException("The password can't be null");

        String email = userInput.getEmail();

        if(!userRepository.findUserByEmail(email).isPresent()) throw new UserNotFoundException(email);
        User user = userRepository.getUserByEmail(email);
        //if(user.getPassword().equals("deactivated")) throw new UserNotFoundException(email);

        if(user.getToken() != null && !user.getToken().equals("AUTH")) throw new AlreadyLoginException();
        
        if(user.getPassword().equals(userInput.getPassword())){
            Map<String, String> result = new HashMap<String, String>();
            String token = RandomString.make(7);
            user.setToken(token);
            userRepository.save(user);
            result.put("userId", String.valueOf(user.getId()));
            result.put("token", token);
            return result;
        }
        else throw new HttpMessageConversionException("Incorrect password.");
    }

    public void userLogout(Long userId, String token) throws LoginException {
        if(!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        User user = userRepository.getById(userId);

        if(user.getToken() != null){
            if(user.getToken().equals(token)){
                user.setToken(null);
                userRepository.save(user);
            }
            else throw new IncorrectTokenException();
        }
        else throw new LoginException();
    }

    public Map<String, String> getToken(Long userId) throws LoginException {
        if(!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        else{
            User user = userRepository.getById(userId);
            if (user.getToken() == null) throw new LoginException();
            Map<String, String> result = new HashMap<String, String>();
            result.put("userId", String.valueOf(user.getId()));
            result.put("token", user.getToken());
            return result;
        }
    }

    public UserOutput getUserByUsername(String username) {
        if(!userRepository.findUserByUsername(username).isPresent()) throw new UserNotFoundException(username);
        User user = userRepository.getUserByUsername(username);
        return new UserOutput(user);
    }

    public UserOutput getUserByEmail(String email) {
        if (!userRepository.findUserByEmail(email).isPresent()) throw new UserNotFoundException(email);
        User user = userRepository.getUserByEmail(email);
        return new UserOutput(user);
    }

    //IMPLEMENTAR TOKENIZACION, LOGIN LOGOUT Y DIFERENTES MANERAS DE LLEGAR AL USUARIO (BY MAIL, USERNAME...)
    //BBDDs Y PROBAR EN LOCAL
    //FRONTEND SENCILLO
    //IMPLEMENTAR EXCEPCIONES







}
