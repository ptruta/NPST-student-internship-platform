package ro.ubbcluj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubbcluj.converter.ApplicationConverter;
import ro.ubbcluj.dto.ApplicationDTO;
import ro.ubbcluj.entity.Application;
import ro.ubbcluj.interfaces.ApplicationService;
import ro.ubbcluj.repository.InternshipAnnouncementRepository;
import ro.ubbcluj.repository.ApplicationRepository;
import ro.ubbcluj.repository.UserAuthenticationRepository;

import java.util.List;

import static ro.ubbcluj.util.ValidationUtil.notNull;

/**
 * This class contains all the business logic
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private InternshipAnnouncementRepository internshipAnnouncementRepository;

    /**
     * Method returns all the applications from the repository
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplications() {
        return ApplicationConverter.convertToDTOList(applicationRepository.findAll());
    }

    /**
     * Method returns a page of applications
     *
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getApplications(Pageable pageable) {
        return ApplicationConverter.convertToDTOPage(applicationRepository.findAll(pageable));
    }

    /**
     * Method returns a users's applications
     *
     * @param username
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByUsername(String username) {
        return ApplicationConverter.convertToDTOList(applicationRepository.findAllByUserAuthenticationAccountUsername(username));
    }

    /**
     * Method returns a page of applications from a announcement
     *
     * @param username
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getApplicationsByUsername(String username, Pageable pageable) {
        notNull(username);
        notNull(pageable);
        notNull(applicationRepository.findByInternshipAnnouncementUserAuthenticationAccountUsername(username, pageable));

        return ApplicationConverter.convertToDTOPage(
                applicationRepository.findByInternshipAnnouncementUserAuthenticationAccountUsername(username, pageable));
    }

    /**
     * Method returns a list of applications from a announcement
     *
     * @param announcementId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> getApplicationsByAnnouncement(Long announcementId) {
        notNull(announcementId);
        notNull(applicationRepository.findAllByInternshipAnnouncementId(announcementId));

        return ApplicationConverter.convertToDTOList(
                applicationRepository.findAllByInternshipAnnouncementId(announcementId));
    }

    /**
     * Method returns a page of applications from a announcement
     *
     * @param announcementId
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationDTO> getApplicationsByAnnouncement(Long announcementId, Pageable pageable) {
        notNull(announcementId);
        notNull(pageable);
        notNull(applicationRepository.findAllByInternshipAnnouncementId(announcementId, pageable));

        return ApplicationConverter.convertToDTOPage(
                applicationRepository.findAllByInternshipAnnouncementId(announcementId, pageable));
    }

    /**
     * Method creates a application
     *
     * @param applicationDTO
     * @return
     */
    @Override
    @Transactional
    public ApplicationDTO createApplication(ApplicationDTO applicationDTO) {
        notNull(applicationDTO);

        Application application = new Application();
        if (applicationDTO.getUsername() != null) {
            application.setUserAuthentication(userAuthenticationRepository.findByAccountUsername(applicationDTO.getUsername()));
        }
        if (applicationDTO.getAnnouncementId() != null) {
            application.setInternshipAnnouncement(internshipAnnouncementRepository.findOne(applicationDTO.getAnnouncementId()));
        }
        if (applicationDTO.getUserId() != null) {
            application.setUserAuthentication(userAuthenticationRepository.findOne(applicationDTO.getUserId()));
        }
        applicationRepository.save(application);

        return ApplicationConverter.convertToDTO(application);
    }

    /**
     * Method returns a application by id
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationDTO getApplicationById(Long id) {
        notNull(id);
        notNull(applicationRepository.findOne(id));
        return ApplicationConverter.convertToDTO(applicationRepository.findOne(id));
    }

}
