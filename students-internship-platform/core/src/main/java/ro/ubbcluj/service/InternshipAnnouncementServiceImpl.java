package ro.ubbcluj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubbcluj.converter.InternshipAnnouncementConverter;
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import ro.ubbcluj.entity.*;
import ro.ubbcluj.exception.GenericException;
import ro.ubbcluj.interfaces.InternshipAnnouncementService;
import ro.ubbcluj.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ro.ubbcluj.util.ValidationUtil.notNull;

/**
 * This class contains all the business logic
 */
@Service
public class InternshipAnnouncementServiceImpl implements InternshipAnnouncementService {

    @Autowired
    private InternshipAnnouncementRepository internshipAnnouncementRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    /**
     * Method returns a list of all announcements
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<InternshipAnnouncementDTO> getAllAnnouncements() {
        List<InternshipAnnouncement> internshipAnnouncements = internshipAnnouncementRepository.findAllByAvailability(true);
        return InternshipAnnouncementConverter.convertToDTOList(internshipAnnouncements);
    }

    /**
     * Method returns a announcement by id
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public InternshipAnnouncementDTO findAnnouncementById(Long id) {
        notNull(id);
        notNull(internshipAnnouncementRepository.findOne(id));

        return InternshipAnnouncementConverter.convertToDTO(internshipAnnouncementRepository.findOne(id));
    }

    /**
     * Method returns a page of all announcements
     *
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InternshipAnnouncementDTO> getAllAnnouncements(Pageable pageable) {
        notNull(pageable);
        notNull(internshipAnnouncementRepository.findAll(pageable));

        return InternshipAnnouncementConverter.convertToDTOPage(
                internshipAnnouncementRepository.findAllByAvailability(true, pageable));
    }

    /**
     * Method creates a DTO containing a announcement and a announcement facility
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public InternshipAnnouncementDTO createAnnouncementDTO(Long id) {
        notNull(id);
        notNull(internshipAnnouncementRepository.findOne(id));

        return InternshipAnnouncementConverter.convertToDTO(internshipAnnouncementRepository.findOne(id));
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

        InternshipAnnouncement internshipAnnouncement = internshipAnnouncementRepository.findOne(id);
        notNull(internshipAnnouncement);

        Account account = internshipAnnouncement.getUserAuthentication().getAccount();
        account.setActive(false);
        accountRepository.save(account);

        internshipAnnouncement.setAvailability(false);
        internshipAnnouncementRepository.save(internshipAnnouncement);
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
    public Page<InternshipAnnouncementDTO> findAnnouncementByCompany(String username, Pageable pageable) {
        notNull(username);

        Page<InternshipAnnouncement> announcements =
                internshipAnnouncementRepository.findByAvailabilityAndUserAuthenticationAccountUsername(true, username, pageable);
        notNull(announcements);

        return InternshipAnnouncementConverter.convertToDTOPage(announcements);
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

        //TODO: list instead of single Long
        return internshipAnnouncementRepository.findByUserAuthenticationAccountUsername(username).getId();
    }

    @Override
    public void applyForAnnouncement(String username, Long announcementId) {
        notNull(announcementId);
        notNull(username);
        UserAuthentication userAuthentication = userAuthenticationRepository.findByAccountUsername(username);
        notNull(userAuthentication);
        InternshipAnnouncement internshipAnnouncement = internshipAnnouncementRepository.findOne(announcementId);
        Date currentDate = new Date();
        if (!currentDate.before(internshipAnnouncement.getDeadline())) {
            throw new GenericException("Sorry, you cannot apply for this announcement. You missed the deadline");
        }
        Application application = Application.builder()
                .userAuthentication(userAuthentication)
                .internshipAnnouncement(internshipAnnouncement)
                .build();
        applicationRepository.save(application);
    }


    /**
     * Method updates announcement info
     *
     * @param internshipAnnouncementDTO
     * @return
     */
    @Override
    @Transactional
    public InternshipAnnouncementDTO updateAnnouncement(InternshipAnnouncementDTO internshipAnnouncementDTO) {
        notNull(internshipAnnouncementDTO);

        final InternshipAnnouncement internshipAnnouncement = internshipAnnouncementRepository.findOne(internshipAnnouncementDTO.getId());

        internshipAnnouncement.setTitle(internshipAnnouncementDTO.getTitle());
        internshipAnnouncement.setStartDate(internshipAnnouncementDTO.getStartDate());
        internshipAnnouncement.setEndDate(internshipAnnouncementDTO.getEndDate());

        internshipAnnouncementRepository.save(internshipAnnouncement);

        return InternshipAnnouncementConverter.convertToDTO(internshipAnnouncement);
    }

    /**
     * Method creates a new announcement
     *
     * @param internshipAnnouncementDTO
     * @return
     */
    @Override
    @Transactional
    public InternshipAnnouncementDTO createAnnouncement(InternshipAnnouncementDTO internshipAnnouncementDTO) {
        notNull(internshipAnnouncementDTO);

        final InternshipAnnouncement internshipAnnouncement = InternshipAnnouncementConverter.convertToEntity(internshipAnnouncementDTO);

        if (internshipAnnouncementDTO.getUsername() != null) {
            Account account = accountRepository.findByUsername(internshipAnnouncementDTO.getUsername());
            UserAuthentication userAuthentication = account.getUserAuthentication();
            internshipAnnouncement.setUserAuthentication(userAuthentication);
        }
        internshipAnnouncement.setAvailability(true);
        internshipAnnouncement.setApplications(new ArrayList<>());

        InternshipAnnouncement savedEntity = internshipAnnouncementRepository.save(internshipAnnouncement);
        return InternshipAnnouncementConverter.convertToDTO(savedEntity);
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
    public Page<InternshipAnnouncementDTO> findAnnouncementsByName(String name, Pageable pageable) {
        return (InternshipAnnouncementConverter.convertToDTOPage(internshipAnnouncementRepository.findByTitle(name, pageable)));
    }

//    @Override
//    @Transactional
//    public Page<InternshipAnnouncementDTO> findAnnouncementsByAnyField(String location,
//                                                                       Date postingDate,
//                                                                       Date startingDate,
//                                                                       Date endDate,
//                                                                       String domains,
//                                                                       boolean paidOrNot,
//                                                                       String duration,
//                                                                       String workingTime,
//                                                                       Pageable pageable) {
//        notNull(location);
//        notNull(postingDate);
//        notNull(startingDate);
//        notNull(endDate);
//        notNull(domains);
//        notNull(paidOrNot);
//        notNull(duration);
//        notNull(workingTime);
//
//        Page<InternshipAnnouncement> announcements = internshipAnnouncementRepository
//                .findAllByLocationOrPostingDateOrStartDateOrEndDateOrDomainsOrPaidOrNotOrDurationOrWorkingTime(location,
//                        postingDate,startingDate,endDate,domains,paidOrNot,duration,workingTime,pageable);
//
//
//        notNull(announcements);
//
//        return InternshipAnnouncementConverter.convertToDTOPage(announcements);

    @Override
    @Transactional
    public List<InternshipAnnouncementDTO> findAnnouncementsByAnyField(String location,
                                                                       Date postingDate,
                                                                       Date startingDate,
                                                                       Date endDate,
                                                                       String domains,
                                                                       boolean paidOrNot,
                                                                       String duration,
                                                                       String workingTime) {
        notNull(location);
        notNull(postingDate);
        notNull(startingDate);
        notNull(endDate);
        notNull(domains);
        notNull(paidOrNot);
        notNull(duration);
        notNull(workingTime);

        //TODO:
        List<InternshipAnnouncement> announcements = internshipAnnouncementRepository.findAll();


        notNull(announcements);

        return InternshipAnnouncementConverter.convertToDTOList(announcements);
    }
}

