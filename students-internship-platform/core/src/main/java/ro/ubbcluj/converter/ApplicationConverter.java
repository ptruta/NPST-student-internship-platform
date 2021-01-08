package ro.ubbcluj.converter;

import org.springframework.data.domain.Page;
import ro.ubbcluj.dto.ApplicationDTO;
import ro.ubbcluj.entity.Application;

import java.util.List;
import java.util.stream.Collectors;

import static ro.ubbcluj.util.ValidationUtil.notNull;

/**
 * The converter class is used to convert Application entity into Application DTO and Application DTO into Application entity
 */
public class ApplicationConverter {

    /**
     * This method converts a Application to a Application DTO
     *
     * @param application
     * @return
     */
    public static ApplicationDTO convertToDTO(Application application) {
        notNull(application);

        return ApplicationDTO.builder()
                .id(application.getId())
                .personEmail(application.getPerson().getEmail())
                .announcementId(application.getAnnouncement().getId())
                .title(application.getAnnouncement().getTitle())
                .username(application.getPerson().getAccount().getUsername())
                .personName(application.getPerson().getFirstName() + " " + application.getPerson().getLastName())
                .userId(application.getPerson().getId())
                .startDate(application.getAnnouncement().getStartDate())
                .endDate(application.getAnnouncement().getEndDate())
                .companyName(application.getAnnouncement().getPerson().getFirstName() + " "
                        + application.getAnnouncement().getPerson().getLastName())
                .build();
    }

    /**
     * This method converts a list of Applications to a list of Application DTOs
     *
     * @param applicationList
     * @return
     */
    public static List<ApplicationDTO> convertToDTOList(List<Application> applicationList) {
        notNull(applicationList);

        return applicationList.stream()
                .map(ApplicationConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * This method converts a page of Applications to a page of Application DTOs
     *
     * @param applicationPage
     * @return
     */
    public static Page<ApplicationDTO> convertToDTOPage(Page<Application> applicationPage) {
        return applicationPage.map(ApplicationConverter::convertToDTO);
    }

}

