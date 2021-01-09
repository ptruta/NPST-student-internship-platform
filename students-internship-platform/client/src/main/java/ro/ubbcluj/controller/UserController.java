package ro.ubbcluj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import ro.ubbcluj.dto.UserAuthenticationDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.InternshipAnnouncementService;
import ro.ubbcluj.interfaces.UserAuthenticationService;
import ro.ubbcluj.model.frontObjects.SearchOption;
import ro.ubbcluj.pagination.PageWrapper;
import ro.ubbcluj.utils.Constants;
import ro.ubbcluj.utils.User;

import java.util.List;

/**
 * This class controls the data flow that goes into users and updates the view whenever data changes
 */
@Controller
@Slf4j
public class UserController {

    private static final String MY_ACCOUNT_PAGE = "/myAccount";
    private static final String USER_MANAGER_PAGE = "/userManagement";
    private final UserAuthenticationService userAuthenticationService;
    private final InternshipAnnouncementService internshipAnnouncementService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Controller
     *
     * @param userAuthenticationService
     * @param internshipAnnouncementService
     * @param bCryptPasswordEncoder
     */
    @Autowired
    public UserController(UserAuthenticationService userAuthenticationService, InternshipAnnouncementService internshipAnnouncementService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userAuthenticationService = userAuthenticationService;
        this.internshipAnnouncementService = internshipAnnouncementService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Method used to display logged user information.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = MY_ACCOUNT_PAGE, method = RequestMethod.GET)
    public String myAccount(Model model) {
        final String username = User.getCurrentUserName();
        final UserAuthenticationDTO userAuthenticationDTO = userAuthenticationService.findByUsername(username);
        model.addAttribute("userAuthenticationDTO", userAuthenticationDTO);
        return "common/" + MY_ACCOUNT_PAGE;
    }

    /**
     * Method used to update the user information.
     *
     * @param userAuthenticationDTO
     * @param model
     * @return
     */
    @RequestMapping(value = MY_ACCOUNT_PAGE, method = RequestMethod.POST)
    public String updateAccount(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO, Model model) {
        userAuthenticationDTO.setPassword(bCryptPasswordEncoder.encode(userAuthenticationDTO.getPassword()));
        userAuthenticationService.updateAccount(userAuthenticationDTO);
        model.addAttribute("userAuthenticationDTO", userAuthenticationDTO);
        return "common/" + MY_ACCOUNT_PAGE;
    }

    /**
     * Method used to redirect to user management page and retrieve a user list.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE, method = RequestMethod.GET)
    public String userManagement(Model model, @PageableDefault(value = Constants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        String username = User.getCurrentUserName();

        PageWrapper<UserAuthenticationDTO> page;
        Page<UserAuthenticationDTO> userDTOPage = null;

        RoleEnum userRole = userAuthenticationService.findByUsername(username).getRole();

//        if (userRole.equals(RoleEnum.ADMIN)) {
//            userDTOPage = userAuthenticationService.getAvailableUsers(pageable);
//            model.addAttribute("announcements", internshipAnnouncementService.getAllAnnouncements());
//            model.addAttribute("searchOption", new SearchOption());
//        }

        if (userRole.equals(RoleEnum.RECRUITER)) {
            // TODO: list of Long instead of Long
            Long announcementId = internshipAnnouncementService.findAnnouncementIdByManager(username);
            userDTOPage = userAuthenticationService.getUsersByAnnouncement(announcementId, pageable);
        }

        page = new PageWrapper<>(userDTOPage, "userManagement");
        model.addAttribute("users", userDTOPage);
        model.addAttribute("page", page);
        return "userManagement" + USER_MANAGER_PAGE;
    }

    /**
     * Method used to search applications by announcement.
     *
     * @param model
     * @param searchOption
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE, method = RequestMethod.POST)
    public String searchByAnnouncement(@ModelAttribute(value = "searchOption") SearchOption searchOption, Model model,
                                       @PageableDefault(value = Constants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        PageWrapper<UserAuthenticationDTO> page;
        Page<UserAuthenticationDTO> userDTOPage;

        if (ObjectUtils.isEmpty(searchOption)
                || searchOption.getAnnouncementId().equals(Integer.toString(Constants.DEFAULT_OPTION))) {
            userDTOPage = userAuthenticationService.getAvailableUsers(pageable);
            page = new PageWrapper<>(userDTOPage, "userManagement/userManagement");
        } else {
            userDTOPage = userAuthenticationService.getUsersByAnnouncement(Long.parseLong(searchOption.getAnnouncementId()),
                    pageable);
            page = new PageWrapper<>(userDTOPage, "userManagement/userManagement");
        }

        model.addAttribute("users", userDTOPage);
        model.addAttribute("announcements", internshipAnnouncementService.getAllAnnouncements());
        model.addAttribute("page", page);
        model.addAttribute("searchOption", searchOption);
        return "userManagement" + USER_MANAGER_PAGE;
    }

    /**
     * Method used to redirect to user management page and retrieve a user list.
     *
     * @param model
     * @param id    : the id of the user that should be deleted.
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE + "/delete/{id}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable Long id, Model model) {
        userAuthenticationService.deleteUser(id);
        model.addAttribute("users", userAuthenticationService.getAvailableUsers());
        return "redirect:" + USER_MANAGER_PAGE;
    }

    /**
     * Method used to redirect to add account.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE + "/addAccount", method = RequestMethod.GET)
    public String addAccount(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO, Model model) {
        final List<InternshipAnnouncementDTO> announcements = internshipAnnouncementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        return "userManagement/addAccount";
    }

    /**
     * Method used to create a new account for an employee or manager.
     *
     * @param userAuthenticationDTO
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE + "/addAccount", method = RequestMethod.POST)
    public String registerAccount(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO) {
        if (userAuthenticationService.checkUsername(userAuthenticationDTO.getUsername())) {
            return "/usernameError";
        } else if (userAuthenticationService.checkEmail(userAuthenticationDTO.getEmail())) {
            return "/emailError";
        } else {
            userAuthenticationDTO.setPassword(bCryptPasswordEncoder.encode(userAuthenticationDTO.getPassword()));
            userAuthenticationService.createUser(userAuthenticationDTO);
            return "redirect:" + USER_MANAGER_PAGE;
        }
    }

    /**
     * Method used to edit account
     *
     * @param id
     * @param model
     * @return
     */

    @RequestMapping(value = USER_MANAGER_PAGE + "/edit/{id}", method = RequestMethod.GET)
    public String editAccount(@PathVariable Long id, Model model) {
        model.addAttribute("userAuthenticationDTO", userAuthenticationService.getById(id));
        return "userManagement/editAccount";
    }

    /**
     * Method used to edit account.
     *
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE + "/edit", method = RequestMethod.POST)
    public String editAccount(@ModelAttribute(value = "userAuthenticationDTO") UserAuthenticationDTO userAuthenticationDTO) {
        userAuthenticationService.editUser(userAuthenticationDTO);
        return "redirect:" + USER_MANAGER_PAGE;
    }

}
