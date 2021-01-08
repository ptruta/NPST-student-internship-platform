package ro.ubbcluj.converter;

import org.springframework.data.domain.Page;
import ro.ubbcluj.dto.AnnouncementDTO;
import ro.ubbcluj.entity.Announcement;
import ro.ubbcluj.util.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The converter class is used to convert Announcement entity into Announcement DTO and Announcement DTO into Announcement entity
 */
public class AnnouncementConverter {

    /**
     * This method converts a Announcement to a Announcement DTO
     *
     * @param announcement
     * @return
     */
    public static AnnouncementDTO convertToDTO(Announcement announcement) {
        ValidationUtil.notNull(announcement);

        return AnnouncementDTO.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .active(announcement.isActive())
                .description(announcement.getDescription())
                .technologies(announcement.getTechnologies())
                .startDate(announcement.getStartDate())
                .endDate(announcement.getEndDate())
                .build();
    }

    /**
     * This method converts a list of Announcements to a list of Announcement DTOs
     *
     * @param announcementList
     * @return
     */
    public static List<AnnouncementDTO> convertToDTOList(List<Announcement> announcementList) {
        ValidationUtil.notNull(announcementList);


        return announcementList.stream()
                .map(AnnouncementConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * This method converts a Announcement DTO to a Announcement
     *
     * @param announcementDTO
     * @return
     */
    public static Announcement convertToEntity(AnnouncementDTO announcementDTO) {
        ValidationUtil.notNull(announcementDTO);

        return Announcement.builder()
                .id(announcementDTO.getId())
                .title(announcementDTO.getTitle())
                .active(announcementDTO.isActive())
                .description(announcementDTO.getDescription())
                .technologies(announcementDTO.getTechnologies())
                .startDate(announcementDTO.getStartDate())
                .endDate(announcementDTO.getEndDate())
                .build();
    }

    /**
     * This method converts a page of Announcements to a page of Announcement DTOs
     *
     * @param announcementPage
     * @return
     */
    public static Page<AnnouncementDTO> convertToDTOPage(Page<Announcement> announcementPage) {
        return announcementPage.map(AnnouncementConverter::convertToDTO);
    }
}
