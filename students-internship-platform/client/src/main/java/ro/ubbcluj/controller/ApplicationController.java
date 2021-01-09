package ro.ubbcluj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import ro.ubbcluj.dto.ApplicationDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.InternshipAnnouncementService;
import ro.ubbcluj.interfaces.ApplicationService;
import ro.ubbcluj.interfaces.UserAuthenticationService;
import ro.ubbcluj.model.frontObjects.ApplicationOption;
import ro.ubbcluj.model.frontObjects.SearchOption;
import ro.ubbcluj.pagination.PageWrapper;
import ro.ubbcluj.utils.Constants;
import ro.ubbcluj.utils.User;

import java.util.List;

/**
 * This class controls the data flow that goes into applications and updates the view whenever data changes
 */
@Controller
@Slf4j
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private InternshipAnnouncementService internshipAnnouncementService;
    @Autowired
    private UserAuthenticationService userAuthenticationService;

    /**
     * Method used to search applications by announcement.
     *
     * @param model
     * @param searchOption
     * @return
     */
    @RequestMapping(value = "/announcementManagement/applicationsManagement", method = RequestMethod.POST)
    public String searchByAnnouncement(@ModelAttribute(value = "searchOption") SearchOption searchOption, Model model,
                                @PageableDefault(value = Constants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        PageWrapper<ApplicationDTO> page;
        Page<ApplicationDTO> applicationDTOPage;

        if (ObjectUtils.isEmpty(searchOption)
                || searchOption.getAnnouncementId().equals(Constants.DEFAULT_STRING_OPTION)) {
            applicationDTOPage = applicationService.getApplications(pageable);
        } else {
            long announcementId = Long.parseLong(searchOption.getAnnouncementId());
            applicationDTOPage = applicationService.getApplicationsByAnnouncement(announcementId, pageable);
        }
        model.addAttribute("applications", applicationDTOPage);
        model.addAttribute("page", new PageWrapper<>(applicationDTOPage, "announcementManagement/applicationsManagement"));
        model.addAttribute("announcements", internshipAnnouncementService.getAllAnnouncements());
        model.addAttribute("searchOption", searchOption);
        return "announcementManagement/applicationsManagement";
    }

    /**
     * Method used to search applications by announcement.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/announcementManagement/applicationsManagement", method = RequestMethod.GET)
    public String announcementManagement(Model model,
                                  @PageableDefault(value = Constants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        String username = User.getCurrentUserName();

        PageWrapper<ApplicationDTO> page;
        Page<ApplicationDTO> applicationDTOPage = null;

        RoleEnum userRole = userAuthenticationService.findByUsername(username).getRole();

        if (userRole.equals(RoleEnum.RECRUITER)) {
            applicationDTOPage = applicationService.getApplications(pageable);
            model.addAttribute("announcements", internshipAnnouncementService.getAllAnnouncements());
            model.addAttribute("searchOption", new SearchOption());
        }

        if (userRole.equals(RoleEnum.APPLICANT)) {
            applicationDTOPage = applicationService.getApplicationsByUsername(username, pageable);
        }

        page = new PageWrapper<>(applicationDTOPage, "/announcementManagement/applicationsManagement");
        model.addAttribute("applications", applicationDTOPage);
        model.addAttribute("page", page);
        return "announcementManagement/applicationsManagement";
    }

    /**
     * Method used to create a new application
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/applications/newApplication", method = RequestMethod.GET)
    public String newApplication(Model model) {
        List<InternshipAnnouncementDTO> allAnnouncements = internshipAnnouncementService.getAllAnnouncements();
        model.addAttribute("announcements", allAnnouncements);
        model.addAttribute("applicationOption", new ApplicationOption());
        model.addAttribute("applicationDTO", new ApplicationDTO());
        return "applications/newApplication";
    }

//    /**
//     * Method used to create a new application
//     *
//     * @param applicationOption
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "/applications/newApplication", method = RequestMethod.POST)
//    public String newApplication(@ModelAttribute(value = "applicationOption") ApplicationOption applicationOption, Model model) {
//        List<AnnouncementDTO> allAnnouncements = announcementService.getAllAnnouncements();
//        model.addAttribute("announcements", allAnnouncements);
//
//        model.addAttribute("applicationOption", applicationOption);
//        ApplicationDTO applicationDTO = new ApplicationDTO();
//        applicationDTO.setAnnouncementId(Long.parseLong(applicationOption.getAnnouncementId()));
//
//        Date startDateTemp = DateFormatter.getDate(applicationOption.getStartDate());
//        Date endDateTemp = DateFormatter.getDate(applicationOption.getEndDate());
//
//        ApplicationValidation.isBefore(startDateTemp, endDateTemp);
//        applicationDTO.setStartDate(startDateTemp);
//
//        applicationDTO.setEndDate(endDateTemp);
//
//        model.addAttribute("rooms", roomService
//                .getAllAvailableRoomsByAnnouncementBetween(applicationDTO.getAnnouncementId(),
//                        applicationDTO.getStartDate(),
//                        applicationDTO.getEndDate())
//                .stream()
//                .filter(Util.distinctByKey(RoomDTO::getSize))
//                .sorted(Comparator.comparing(RoomDTO::getSize))
//                .collect(Collectors.toList()));
//
//        model.addAttribute("applicationDTO", applicationDTO);
//
//        return "applications/newApplication";
//    }

    /**
     * Method used to load current user applications and display them.
     *
     * @param applicationDTO
     * @param model
     * @return
     */
    @RequestMapping(value = "/applications/myApplications", method = RequestMethod.GET)
    public String currentUserApplications(@ModelAttribute(value = "applicationDTO") ApplicationDTO applicationDTO, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final List<ApplicationDTO> applications = applicationService.getApplicationsByUsername(auth.getName());
        model.addAttribute("applications", applications);
        model.addAttribute("applicationOption", new ApplicationOption());
        return "applications/myApplications";
    }

}
