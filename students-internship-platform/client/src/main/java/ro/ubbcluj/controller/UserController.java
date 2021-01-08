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
import ro.ubbcluj.dto.AnnouncementDTO;
import ro.ubbcluj.dto.UserDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.AnnouncementService;
import ro.ubbcluj.interfaces.UserService;
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
    private final UserService userService;
    private final AnnouncementService announcementService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Controller
     *
     * @param userService
     * @param announcementService
     * @param bCryptPasswordEncoder
     */
    @Autowired
    public UserController(UserService userService, AnnouncementService announcementService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.announcementService = announcementService;
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
        final UserDTO userDTO = userService.findByUsername(username);
        model.addAttribute("userDTO", userDTO);
        return "common/" + MY_ACCOUNT_PAGE;
    }

    /**
     * Method used to update the user information.
     *
     * @param userDTO
     * @param model
     * @return
     */
    @RequestMapping(value = MY_ACCOUNT_PAGE, method = RequestMethod.POST)
    public String updateAccount(@ModelAttribute(value = "userDTO") UserDTO userDTO, Model model) {
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        userService.updateAccount(userDTO);
        model.addAttribute("userDTO", userDTO);
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

        PageWrapper<UserDTO> page;
        Page<UserDTO> userDTOPage = null;

        RoleEnum userRole = userService.findByUsername(username).getRole();

        if (userRole.equals(RoleEnum.ADMIN)) {
            userDTOPage = userService.getAvailableUsers(pageable);
            model.addAttribute("announcements", announcementService.getAllAnnouncements());
            model.addAttribute("searchOption", new SearchOption());
        }

        if (userRole.equals(RoleEnum.COMPANY)) {
            Long announcementId = announcementService.findAnnouncementIdByManager(username);
            userDTOPage = userService.getUsersByAnnouncement(announcementId, pageable);
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
        PageWrapper<UserDTO> page;
        Page<UserDTO> userDTOPage;

        if (ObjectUtils.isEmpty(searchOption)
                || searchOption.getAnnouncementId().equals(Integer.toString(Constants.DEFAULT_OPTION))) {
            userDTOPage = userService.getAvailableUsers(pageable);
            page = new PageWrapper<>(userDTOPage, "userManagement/userManagement");
        } else {
            userDTOPage = userService.getUsersByAnnouncement(Long.parseLong(searchOption.getAnnouncementId()),
                    pageable);
            page = new PageWrapper<>(userDTOPage, "userManagement/userManagement");
        }

        model.addAttribute("users", userDTOPage);
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
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
        userService.deleteUser(id);
        model.addAttribute("users", userService.getAvailableUsers());
        return "redirect:" + USER_MANAGER_PAGE;
    }

    /**
     * Method used to redirect to add account.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE + "/addAccount", method = RequestMethod.GET)
    public String addAccount(@ModelAttribute(value = "userDTO") UserDTO userDTO, Model model) {
        final List<AnnouncementDTO> announcements = announcementService.getAllAnnouncements();
        model.addAttribute("announcements", announcements);
        return "userManagement/addAccount";
    }

    /**
     * Method used to create a new account for an employee or manager.
     *
     * @param userDTO
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE + "/addAccount", method = RequestMethod.POST)
    public String registerAccount(@ModelAttribute(value = "userDTO") UserDTO userDTO) {
        if (userService.checkUsername(userDTO.getUsername())) {
            return "/usernameError";
        } else if (userService.checkEmail(userDTO.getEmail())) {
            return "/emailError";
        } else {
            userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            userService.createUser(userDTO);
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
        model.addAttribute("userDTO", userService.getById(id));
        return "userManagement/editAccount";
    }

    /**
     * Method used to edit account.
     *
     * @return
     */
    @RequestMapping(value = USER_MANAGER_PAGE + "/edit", method = RequestMethod.POST)
    public String editAccount(@ModelAttribute(value = "userDTO") UserDTO userDTO) {
        userService.editUser(userDTO);
        return "redirect:" + USER_MANAGER_PAGE;
    }

}
