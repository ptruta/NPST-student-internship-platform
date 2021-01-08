package ro.ubbcluj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.ubbcluj.dto.AnnouncementDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.AnnouncementService;
import ro.ubbcluj.interfaces.ApplicationService;
import ro.ubbcluj.interfaces.UserService;
import ro.ubbcluj.pagination.PageWrapper;
import ro.ubbcluj.utils.ApplicationValidation;
import ro.ubbcluj.utils.Constants;
import ro.ubbcluj.utils.User;

/**
 * This class controls the data flow that goes into announcements and updates the view whenever data changes
 */

@Controller
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    /**
     * Method used to redirect to announcement management page and retrieve a announcement list.
     *
     * @param model
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/announcementManagement", method = RequestMethod.GET)
    public String announcementManagement(Model model, final @PageableDefault(value = Constants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        String username = User.getCurrentUserName();

        Page<AnnouncementDTO> announcementDTOPage = null;
        PageWrapper<AnnouncementDTO> page;

        if (!model.containsAttribute("announcements")) {

            RoleEnum userRole = userService.findByUsername(username).getRole();

            if (userRole.equals(RoleEnum.ADMIN) || userRole.equals(RoleEnum.STUDENT)) {
                announcementDTOPage = announcementService.getAllAnnouncements(pageable);
            }

            if (userRole.equals(RoleEnum.COMPANY)) {
                announcementDTOPage = announcementService.findAnnouncementByCompany(username, pageable);
            }

            page = new PageWrapper<>(announcementDTOPage, "/announcementManagement/announcementManagement");
            model.addAttribute("announcements", announcementDTOPage);
            model.addAttribute("page", page);

        }

        return "announcementManagement/announcementManagement";
    }

    /**
     * Method used view the announcement information
     *
     * @param model
     * @param id    : the id of the announcement that should be viewed
     * @return
     */
    @RequestMapping(value = "/announcementManagement/announcementInfo/{id}", method = RequestMethod.GET)
    public String showUpdateAnnouncementForm(@PathVariable Long id, Model model) {
        model.addAttribute("announcementDTO", announcementService.createAnnouncementDTO(id));
        return "announcementManagement/announcementInformation";
    }

    /**
     * Method used view the announcement information
     *
     * @param announcementDTO
     * @return
     */
    @RequestMapping(value = "/announcementManagement/announcementInfo", method = RequestMethod.POST)
    public String updateAnnouncement(@ModelAttribute(value = "announcementDTO") AnnouncementDTO announcementDTO, Model model) {
        ApplicationValidation.isBefore(announcementDTO.getStartDate(), announcementDTO.getEndDate());
        announcementService.updateAnnouncement(announcementDTO);
        return "redirect:/announcementManagement";
    }

    /**
     * Method used to delete a announcement
     *
     * @param model
     * @param announcementId    : the announcementId of the announcement that should be deleted
     * @return
     */
    @RequestMapping(value = "/announcementManagement/apply/{announcementId}", method = RequestMethod.POST)
    public String applyForAnnouncement(@PathVariable Long announcementId, Model model) {
        String currentUserName = User.getCurrentUserName();
        announcementService.applyForAnnouncement(currentUserName, announcementId);
        return "redirect:/applications/myApplications";
    }

    /**
     * Method used to delete a announcement
     *
     * @param model
     * @param id    : the id of the announcement that should be deleted
     * @return
     */
    @RequestMapping(value = "/announcementManagement/delete/{id}", method = RequestMethod.POST)
    public String deleteAnnouncement(@PathVariable Long id, Model model) {
        announcementService.deleteAnnouncement(id);
        return "redirect:/announcementManagement";
    }

    /**
     * Method used to add announcement
     *
     * @param announcementDTO
     */
    @RequestMapping(value = "/announcementManagement/addAnnouncement", method = RequestMethod.POST)
    public String addAnnouncement(@ModelAttribute(value = "announcementDTO") AnnouncementDTO announcementDTO) {
        String username = User.getCurrentUserName();
        announcementDTO.setUsername(username);
        announcementService.createAnnouncement(announcementDTO);
        return "redirect:/announcementManagement/";
    }

    /**
     * Method used to add announcement
     *
     * @param model
     */
    @RequestMapping(value = "/announcementManagement/addAnnouncement", method = RequestMethod.GET)
    public String showAddAnnouncementForm(Model model) {
        model.addAttribute("announcementDTO", new AnnouncementDTO());
        return "announcementManagement/addAnnouncement";
    }

    /**
     * Method used to search announcements by title.
     *
     * @param announcementSearch
     * @param pageable
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/announcementManagement/searchAnnouncement", method = RequestMethod.POST)
    public String searchAnnouncement(@ModelAttribute(value = "announcementSearch") String announcementSearch,
                                     final @PageableDefault(value = Constants.DEFAULT_PAGE_SIZE) Pageable pageable,
                                     RedirectAttributes redirectAttributes) {
        final Page<AnnouncementDTO> announcementDTOPage = announcementService.findAnnouncementsByName(announcementSearch, pageable);
        final PageWrapper<AnnouncementDTO> page = new PageWrapper<>(announcementDTOPage, "/announcementManagement/announcementSearch");
        redirectAttributes.addFlashAttribute("announcements", announcementDTOPage);
        redirectAttributes.addFlashAttribute("page", page);
        return "redirect:/announcementManagement";
    }

}
