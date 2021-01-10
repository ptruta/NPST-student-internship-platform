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
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import ro.ubbcluj.model.frontObjects.InternshipAnnouncementSearchDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.ApplicationService;
import ro.ubbcluj.interfaces.InternshipAnnouncementService;
import ro.ubbcluj.interfaces.UserAuthenticationService;
import ro.ubbcluj.pagination.PageWrapper;
import ro.ubbcluj.utils.ApplicationValidation;
import ro.ubbcluj.utils.Constants;
import ro.ubbcluj.utils.User;

import java.util.List;

/**
 * This class controls the data flow that goes into announcements and updates the view whenever data changes
 */

@Controller
public class InternshipAnnouncementController {

    @Autowired
    private InternshipAnnouncementService internshipAnnouncementService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

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

//        Page<InternshipAnnouncementDTO> announcementDTOPage = null;
//        PageWrapper<InternshipAnnouncementDTO> page;
//
//        if (!model.containsAttribute("announcements")) {
//
//            RoleEnum userRole = userAuthenticationService.findByUsername(username).getRole();
//
//            if (userRole.equals(RoleEnum.APPLICANT)) {
//                announcementDTOPage = internshipAnnouncementService.getAllAnnouncements(pageable);
//            }
//
//            if (userRole.equals(RoleEnum.RECRUITER)) {
//                announcementDTOPage = internshipAnnouncementService.findAnnouncementByCompany(username, pageable);
//            }
//
//            page = new PageWrapper<>(announcementDTOPage, "/announcementManagement/announcementManagement");
//            model.addAttribute("announcements", announcementDTOPage);
//            model.addAttribute("page", page);
//        }

        model.addAttribute("announcements", internshipAnnouncementService.getAllAnnouncements());
        model.addAttribute("internshipAnnouncementSearchDTO", new InternshipAnnouncementSearchDTO());
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
        model.addAttribute("announcementDTO", internshipAnnouncementService.createAnnouncementDTO(id));
        return "announcementManagement/announcementInformation";
    }

    /**
     * Method used view the announcement information
     *
     * @param internshipAnnouncementDTO
     * @return
     */
    @RequestMapping(value = "/announcementManagement/announcementInfo", method = RequestMethod.POST)
    public String updateAnnouncement(@ModelAttribute(value = "announcementDTO") InternshipAnnouncementDTO internshipAnnouncementDTO, Model model) {
        ApplicationValidation.isBefore(internshipAnnouncementDTO.getStartDate(), internshipAnnouncementDTO.getEndDate());
        internshipAnnouncementService.updateAnnouncement(internshipAnnouncementDTO);
        return "redirect:/announcementManagement";
    }

    /**
     * Method used to delete a announcement
     *
     * @param model
     * @param announcementId : the announcementId of the announcement that should be deleted
     * @return
     */
    @RequestMapping(value = "/announcementManagement/apply/{announcementId}", method = RequestMethod.POST)
    public String applyForAnnouncement(@PathVariable Long announcementId, Model model) {
        String currentUserName = User.getCurrentUserName();
        internshipAnnouncementService.applyForAnnouncement(currentUserName, announcementId);
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
        internshipAnnouncementService.deleteAnnouncement(id);
        return "redirect:/announcementManagement";
    }

    /**
     * Method used to add announcement
     *
     * @param internshipAnnouncementDTO
     */
    @RequestMapping(value = "/announcementManagement/addAnnouncement", method = RequestMethod.POST)
    public String addAnnouncement(@ModelAttribute(value = "announcementDTO") InternshipAnnouncementDTO internshipAnnouncementDTO) {
        String username = User.getCurrentUserName();
        internshipAnnouncementDTO.setUsername(username);
        internshipAnnouncementService.createAnnouncement(internshipAnnouncementDTO);
        return "redirect:/announcementManagement/";
    }

    /**
     * Method used to add announcement
     *
     * @param model
     */
    @RequestMapping(value = "/announcementManagement/addAnnouncement", method = RequestMethod.GET)
    public String showAddAnnouncementForm(Model model) {
        model.addAttribute("announcementDTO", new InternshipAnnouncementDTO());
        return "announcementManagement/addAnnouncement";
    }

    /**
     * Method used to search announcements by location.
     *
     * @param internshipAnnouncementSearchDTO
     * @param redirectAttributes
     * @return
     */
    @Autowired
    @RequestMapping(value = "/announcementManagement/searchAnnouncement", method = RequestMethod.POST)
    public String searchAnnouncement(@ModelAttribute(value = "internshipAnnouncementSearchDTO") InternshipAnnouncementSearchDTO internshipAnnouncementSearchDTO,
                                     RedirectAttributes redirectAttributes, Model model) {
        List<InternshipAnnouncementDTO> announcementDTOPage = internshipAnnouncementService.findAnnouncementsByAnyField(internshipAnnouncementSearchDTO.getLocation(),
                internshipAnnouncementSearchDTO.getStartDate(),
                internshipAnnouncementSearchDTO.getEndDate(),
                internshipAnnouncementSearchDTO.getPostingDate(),
                internshipAnnouncementSearchDTO.getDuration(),
                internshipAnnouncementSearchDTO.isPaidOrNot(),
                internshipAnnouncementSearchDTO.getDomains(),
                internshipAnnouncementSearchDTO.getWorkingTime());
        //final PageWrapper<InternshipAnnouncementDTO> page = new PageWrapper<InternshipAnnouncementDTO>(announcementDTOPage, "/announcementManagement/internshipAnnouncementSearchDTO");
        model.addAttribute("announcements", announcementDTOPage);
        //model.addAttribute("page", page);
        model.addAttribute("internshipAnnouncementSearchDTO", internshipAnnouncementSearchDTO);

        redirectAttributes.addFlashAttribute("announcements", announcementDTOPage);
        //redirectAttributes.addFlashAttribute("page", page);
        redirectAttributes.addFlashAttribute("internshipAnnouncementSearchDTO", internshipAnnouncementSearchDTO);

        return "redirect:/announcementManagement";
    }

}
