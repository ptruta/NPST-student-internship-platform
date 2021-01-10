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
import ro.ubbcluj.dto.UserAuthenticationDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.exception.HMSMailException;
import ro.ubbcluj.interfaces.EmailService;
import ro.ubbcluj.interfaces.UserAuthenticationService;

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
    private UserAuthenticationService userAuthenticationService;
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
     * @param userAuthenticationDTO
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegister(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO) {
        return "common/register";
    }

    /**
     * Method used to register a new user
     *
     * @param userAuthenticationDTO
     * @return
     * @throws HMSMailException
     */

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO) throws HMSMailException {

        if (userAuthenticationService.checkUsername(userAuthenticationDTO.getUsername())) {
            return "/usernameError";

        } else if (userAuthenticationService.checkEmail(userAuthenticationDTO.getEmail())) {
            return "/emailError";

        } else {
           // emailService.sendEmail(userAuthenticationDTO.getEmail(), ANNOUNCEMENT_EMAIL, SUBJECT, TEXT);
            userAuthenticationDTO.setPassword(bCryptPasswordEncoder.encode(userAuthenticationDTO.getPassword()));
            if (("APPLICANT").equals(userAuthenticationDTO.getStatus())) {
                userAuthenticationDTO.setRole(RoleEnum.APPLICANT);
            }
            else {
                userAuthenticationDTO.setRole(RoleEnum.RECRUITER);
            }
            userAuthenticationDTO.setRegistrationDate(new Date());
            userAuthenticationDTO.setAvailability(true);
            userAuthenticationService.createUser(userAuthenticationDTO);
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

    @RequestMapping(value = "/advice", method = RequestMethod.GET)
    public String advice(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO) {
        return "common/advice";
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO) {
        return "common/help";
    }
}
