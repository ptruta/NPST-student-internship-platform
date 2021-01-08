package ro.ubbcluj.interfaces;

import ro.ubbcluj.dto.ApplicationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {

    List<ApplicationDTO> getApplications();

    Page<ApplicationDTO> getApplications(Pageable pageable);

    List<ApplicationDTO> getApplicationsByUsername(String username);

    Page<ApplicationDTO> getApplicationsByUsername(String username, Pageable pageable);

    List<ApplicationDTO> getApplicationsByAnnouncement(Long announcementId);

    Page<ApplicationDTO> getApplicationsByAnnouncement(Long announcementId, Pageable pageable);

    ApplicationDTO createApplication(ApplicationDTO applicationDTO);

    ApplicationDTO getApplicationById(Long id);

}


