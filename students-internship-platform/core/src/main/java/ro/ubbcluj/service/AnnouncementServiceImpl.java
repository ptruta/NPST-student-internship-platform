package ro.ubbcluj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubbcluj.converter.AnnouncementConverter;
import ro.ubbcluj.dto.AnnouncementDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.Announcement;
import ro.ubbcluj.entity.Application;
import ro.ubbcluj.entity.Person;
import ro.ubbcluj.exception.GenericException;
import ro.ubbcluj.interfaces.AnnouncementService;
import ro.ubbcluj.repository.AccountRepository;
import ro.ubbcluj.repository.AnnouncementRepository;
import ro.ubbcluj.repository.ApplicationRepository;
import ro.ubbcluj.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ro.ubbcluj.util.ValidationUtil.notNull;

/**
 * This class contains all the business logic
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * Method returns a list of all announcements
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementDTO> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAllByActive(true);
        return AnnouncementConverter.convertToDTOList(announcements);
    }

    /**
     * Method returns a announcement by id
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public AnnouncementDTO findAnnouncementById(Long id) {
        notNull(id);
        notNull(announcementRepository.findOne(id));

        return AnnouncementConverter.convertToDTO(announcementRepository.findOne(id));
    }

    /**
     * Method returns a page of all announcements
     *
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementDTO> getAllAnnouncements(Pageable pageable) {
        notNull(pageable);
        notNull(announcementRepository.findAll(pageable));

        return AnnouncementConverter.convertToDTOPage(
                announcementRepository.findAllByActive(true, pageable));
    }

    /**
     * Method creates a DTO containing a announcement and a announcement facility
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public AnnouncementDTO createAnnouncementDTO(Long id) {
        notNull(id);
        notNull(announcementRepository.findOne(id));

        return AnnouncementConverter.convertToDTO(announcementRepository.findOne(id));
    }

    /**
     * Method deletes a announcement
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        notNull(id);

        Announcement announcement = announcementRepository.findOne(id);
        notNull(announcement);

        Account account = announcement.getPerson().getAccount();
        account.setActive(false);
        accountRepository.save(account);

        announcement.setActive(false);
        announcementRepository.save(announcement);
    }

    /**
     * Method that finds the announcement that manager works for
     *
     * @param username
     * @param pageable
     * @return
     */
    @Override
    @Transactional
    public Page<AnnouncementDTO> findAnnouncementByCompany(String username, Pageable pageable) {
        notNull(username);

        Page<Announcement> announcements =
                announcementRepository.findByActiveAndPerson_Account_Username(true, username, pageable);
        notNull(announcements);

        return AnnouncementConverter.convertToDTOPage(announcements);
    }

    /**
     * Method that finds the announcement that manager works for
     *
     * @param username
     * @return
     */
    @Override
    @Transactional
    public Long findAnnouncementIdByManager(String username) {
        notNull(username);

        return announcementRepository.findByPerson_Account_Username(username).getId();
    }

    @Override
    public void applyForAnnouncement(String username, Long announcementId) {
        notNull(announcementId);
        notNull(username);
        Person person = personRepository.findByAccount_Username(username);
        notNull(person);
        Announcement announcement = announcementRepository.findOne(announcementId);
        Date currentDate = new Date();
        if (!currentDate.before(announcement.getDeadline())) {
            throw new GenericException("Sorry, you cannot apply for this announcement. You missed the deadline");
        }
        Application application = Application.builder()
                .person(person)
                .announcement(announcement)
                .build();
        applicationRepository.save(application);
    }

    /**
     * Method updates announcement info
     *
     * @param announcementDTO
     * @return
     */
    @Override
    @Transactional
    public AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO) {
        notNull(announcementDTO);

        final Announcement announcement = announcementRepository.findOne(announcementDTO.getId());

        announcement.setTitle(announcementDTO.getTitle());
        announcement.setDescription(announcementDTO.getDescription());
        announcement.setTechnologies(announcementDTO.getTechnologies());
        announcement.setStartDate(announcementDTO.getStartDate());
        announcement.setEndDate(announcementDTO.getEndDate());

        announcementRepository.save(announcement);

        return AnnouncementConverter.convertToDTO(announcement);
    }

    /**
     * Method creates a new announcement
     *
     * @param announcementDTO
     * @return
     */
    @Override
    @Transactional
    public AnnouncementDTO createAnnouncement(AnnouncementDTO announcementDTO) {
        notNull(announcementDTO);

        final Announcement announcement = AnnouncementConverter.convertToEntity(announcementDTO);

        if (announcementDTO.getUsername() != null) {
            Account account = accountRepository.findByUsername(announcementDTO.getUsername());
            Person person = account.getPerson();
            announcement.setPerson(person);
        }
        announcement.setActive(true);
        announcement.setApplications(new ArrayList<>());

        Announcement savedEntity = announcementRepository.save(announcement);
        return AnnouncementConverter.convertToDTO(savedEntity);
    }

    /**
     * Method finds a announcement by title
     *
     * @param name
     * @param pageable
     * @return
     */
    @Override
    @Transactional
    public Page<AnnouncementDTO> findAnnouncementsByName(String name, Pageable pageable) {
        return (AnnouncementConverter.convertToDTOPage(announcementRepository.findByTitle(name, pageable)));
    }

}
