package ro.ubbcluj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.ubbcluj.dto.UserDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.exception.HMSMailException;
import ro.ubbcluj.interfaces.EmailService;
import ro.ubbcluj.interfaces.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * This class controls the login and register page and updates the view whenever data changes
 */

@Slf4j
@Controller
public class IndexController {
    private static final String ANNOUNCEMENT_EMAIL = "applicationsystem802@gmail.com";
    private static final String SUBJECT = "Registration confirmation";
    private static final String TEXT = "Your account has been successfully created!";
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;

    /**
     * Method used to redirect to the index page.
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    /**
     * Method used to log in the user.
     *
     * @param model
     * @param error
     * @param logout
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            log.error("Your username and password is invalid.");
            String message = "Invalid username or password, try again !";
            model.addAttribute("message", message);
        }
        if (logout != null)
            log.info("You have been logged out successfully.");

        return "index";
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    /**
     * Method used to redirect to the access denied page
     *
     * @return
     */
    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied() {
        return "/accessDenied";
    }

    /**
     * Method used to redirect to duplicate username error page
     *
     * @return
     */
    @RequestMapping(value = "/usernameError", method = RequestMethod.GET)
    public String usernameError() {
        return "/usernameError";
    }

    /**
     * Method used to redirect to duplicate email error page
     *
     * @return
     */
    @RequestMapping(value = "/emailError", method = RequestMethod.GET)
    public String emailError() {
        return "/emailError";
    }

    /**
     * Method used to show the register page.
     *
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegister(@ModelAttribute(value = "userDTO") UserDTO userDTO) {
        return "common/register";
    }

    /**
     * Method used to register a new user
     *
     * @param userDTO
     * @return
     * @throws HMSMailException
     */

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute(value = "userDTO") UserDTO userDTO) throws HMSMailException {

        if (userService.checkUsername(userDTO.getUsername())) {
            return "/usernameError";

        } else if (userService.checkEmail(userDTO.getEmail())) {
            return "/emailError";

        } else {
            emailService.sendEmail(userDTO.getEmail(), ANNOUNCEMENT_EMAIL, SUBJECT, TEXT);
            userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            userDTO.setRole(RoleEnum.STUDENT);
            userDTO.setRegistrationDate(new Date());
            userDTO.setActive(true);
            userService.createUser(userDTO);
            return "common/userRegistered";
        }
    }

    /**
     * Method used to log out the user
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
