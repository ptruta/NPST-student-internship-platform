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
import ro.ubbcluj.repository.AnnouncementRepository;
import ro.ubbcluj.repository.ApplicationRepository;
import ro.ubbcluj.repository.PersonRepository;

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
    private PersonRepository personRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

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
        return ApplicationConverter.convertToDTOList(applicationRepository.findAllByPerson_Account_Username(username));
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
        notNull(applicationRepository.findByAnnouncement_Person_Account_Username(username, pageable));

        return ApplicationConverter.convertToDTOPage(
                applicationRepository.findByAnnouncement_Person_Account_Username(username, pageable));
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
        notNull(applicationRepository.findAllByAnnouncement_Id(announcementId));

        return ApplicationConverter.convertToDTOList(
                applicationRepository.findAllByAnnouncement_Id(announcementId));
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
        notNull(applicationRepository.findAllByAnnouncement_Id(announcementId, pageable));

        return ApplicationConverter.convertToDTOPage(
                applicationRepository.findAllByAnnouncement_Id(announcementId, pageable));
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
            application.setPerson(personRepository.findByAccount_Username(applicationDTO.getUsername()));
        }
        if (applicationDTO.getAnnouncementId() != null) {
            application.setAnnouncement(announcementRepository.findOne(applicationDTO.getAnnouncementId()));
        }
        if (applicationDTO.getUserId() != null) {
            application.setPerson(personRepository.findOne(applicationDTO.getUserId()));
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
