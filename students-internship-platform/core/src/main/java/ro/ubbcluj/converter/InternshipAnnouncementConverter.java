package ro.ubbcluj.converter;

import org.springframework.data.domain.Page;
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import ro.ubbcluj.entity.InternshipAnnouncement;
import ro.ubbcluj.util.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The converter class is used to convert Announcement entity into Announcement DTO and Announcement DTO into Announcement entity
 */
public class InternshipAnnouncementConverter {

    /**
     * This method converts a Announcement to a Announcement DTO
     *
     * @param internshipAnnouncement
     * @return
     */
    public static InternshipAnnouncementDTO convertToDTO(InternshipAnnouncement internshipAnnouncement) {
        ValidationUtil.notNull(internshipAnnouncement);

        return InternshipAnnouncementDTO.builder()
                .id(internshipAnnouncement.getId())
                .title(internshipAnnouncement.getTitle())
                .availability(internshipAnnouncement.isAvailability())
                .availabilityOfTrainingCourse(internshipAnnouncement.getAvailabilityOfTrainingCourse())
                .benefits(internshipAnnouncement.getBenefits())
                .company(internshipAnnouncement.getCompany())
                .postingDate(internshipAnnouncement.getPostingDate())
                .domains(internshipAnnouncement.getDomains())
                .duration(internshipAnnouncement.getDuration())
                .endDate(internshipAnnouncement.getEndDate())
                .startDate(internshipAnnouncement.getStartDate())
                .location(internshipAnnouncement.getLocation())
                .neededSkills(internshipAnnouncement.getNeededSkills())
                .numberOfPositions(internshipAnnouncement.getNumberOfPositions())
                .paidOrNot(internshipAnnouncement.isPaidOrNot())
                .possibilityOfContract(internshipAnnouncement.getPossibilityOfContract())
                .possibilityOfRemoteWork(internshipAnnouncement.getPossibilityOfRemoteWork())
                .requirements(internshipAnnouncement.getRequirements())
                .workingTime(internshipAnnouncement.getWorkingTime())
                .startDate(internshipAnnouncement.getStartDate())
                .endDate(internshipAnnouncement.getEndDate())
                .postingDate(internshipAnnouncement.getPostingDate())
                .deadline(internshipAnnouncement.getDeadline())
                .username(internshipAnnouncement.getUsername())
                .build();
    }

    /**
     * This method converts a list of Announcements to a list of Announcement DTOs
     *
     * @param internshipAnnouncementList
     * @return
     */
    public static List<InternshipAnnouncementDTO> convertToDTOList(List<InternshipAnnouncement> internshipAnnouncementList) {
        ValidationUtil.notNull(internshipAnnouncementList);


        return internshipAnnouncementList.stream()
                .map(InternshipAnnouncementConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * This method converts a Announcement DTO to a Announcement
     *
     * @param internshipAnnouncementDTO
     * @return
     */
    public static InternshipAnnouncement convertToEntity(InternshipAnnouncementDTO internshipAnnouncementDTO) {
        ValidationUtil.notNull(internshipAnnouncementDTO);

        return InternshipAnnouncement.builder()
                .id(internshipAnnouncementDTO.getId())
                .title(internshipAnnouncementDTO.getTitle())
                .title(internshipAnnouncementDTO.getTitle())
                .availability(internshipAnnouncementDTO.isAvailability())
                .availabilityOfTrainingCourse(internshipAnnouncementDTO.getAvailabilityOfTrainingCourse())
                .benefits(internshipAnnouncementDTO.getBenefits())
                .company(internshipAnnouncementDTO.getCompany())
                .postingDate(internshipAnnouncementDTO.getPostingDate())
                .domains(internshipAnnouncementDTO.getDomains())
                .duration(internshipAnnouncementDTO.getDuration())
                .endDate(internshipAnnouncementDTO.getEndDate())
                .startDate(internshipAnnouncementDTO.getStartDate())
                .location(internshipAnnouncementDTO.getLocation())
                .neededSkills(internshipAnnouncementDTO.getNeededSkills())
                .numberOfPositions(internshipAnnouncementDTO.getNumberOfPositions())
                .paidOrNot(internshipAnnouncementDTO.isPaidOrNot())
                .possibilityOfContract(internshipAnnouncementDTO.getPossibilityOfContract())
                .possibilityOfRemoteWork(internshipAnnouncementDTO.getPossibilityOfRemoteWork())
                .requirements(internshipAnnouncementDTO.getRequirements())
                .workingTime(internshipAnnouncementDTO.getWorkingTime())
                .startDate(internshipAnnouncementDTO.getStartDate())
                .endDate(internshipAnnouncementDTO.getEndDate())
                .deadline(internshipAnnouncementDTO.getDeadline())
                .username(internshipAnnouncementDTO.getUsername())
                .build();
    }

    /**
     * This method converts a page of Announcements to a page of Announcement DTOs
     *
     * @param announcementPage
     * @return
     */
    public static Page<InternshipAnnouncementDTO> convertToDTOPage(Page<InternshipAnnouncement> announcementPage) {
        return announcementPage.map(InternshipAnnouncementConverter::convertToDTO);
    }
}
