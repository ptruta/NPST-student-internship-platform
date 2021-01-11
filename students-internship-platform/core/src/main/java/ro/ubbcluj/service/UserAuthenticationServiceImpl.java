package ro.ubbcluj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ro.ubbcluj.converter.UserAuthenticationConverter;
import ro.ubbcluj.dto.UserAuthenticationDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.Role;
import ro.ubbcluj.entity.UserAuthentication;
import ro.ubbcluj.interfaces.UserAuthenticationService;
import ro.ubbcluj.repository.*;
import ro.ubbcluj.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

/**
 * This class contains all the business logic
 */
@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private InternshipAnnouncementRepository internshipAnnouncementRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    /**
     * Method creates a new user DTO
     *
     * @param userAuthenticationDTO
     * @return
     */
    @Override
    @Transactional
    public UserAuthenticationDTO createUser(UserAuthenticationDTO userAuthenticationDTO) {
        ValidationUtil.notNull(userAuthenticationDTO);

        UserAuthentication userAuthentication = getUserAuthentication(userAuthenticationDTO);

        Account account = getAccount(userAuthenticationDTO, userAuthentication);
        userAuthentication.setRole(new Role(userAuthenticationDTO.getRole()));
        userAuthentication.setFirstName(userAuthenticationDTO.getFirstName());
        userAuthentication.setLastName(userAuthenticationDTO.getLastName());
        userAuthentication.setEmail(userAuthenticationDTO.getEmail());
        userAuthentication.setSkills(userAuthenticationDTO.getSkills());
        userAuthentication.setContact(userAuthenticationDTO.getContact());
        userAuthentication.setDomainsOfInterest(userAuthenticationDTO.getDomainsOInterest());
        userAuthentication.setEducation(userAuthenticationDTO.getEducation());
        userAuthentication.setVolunteerExperience(userAuthenticationDTO.getVolunteerExperience());
        userAuthentication.setHobbies(userAuthenticationDTO.getHobbies());
        userAuthentication.setWorkHistory(userAuthenticationDTO.getWorkHistory());
        userAuthentication.setAvailability(true);
        userAuthenticationRepository.save(userAuthentication);
        accountRepository.save(account);

        return userAuthenticationDTO;
    }

    /**
     * Method used to return the account of a person
     *
     * @param userAuthenticationDTO
     * @param userAuthentication
     * @return
     */
    private Account getAccount(UserAuthenticationDTO userAuthenticationDTO, UserAuthentication userAuthentication) {
        ValidationUtil.notNull(userAuthenticationDTO);
        ValidationUtil.notNull(userAuthentication);

        Account account = UserAuthenticationConverter.convertToEntityAccount(userAuthenticationDTO);
        account.setUserAuthentication(userAuthentication);
        account.setActive(true);
        return account;
    }

    /**
     * Method used to extract a person from a user DTO
     *
     * @param userAuthenticationDTO
     * @return
     */
    private UserAuthentication getUserAuthentication(UserAuthenticationDTO userAuthenticationDTO) {
        ValidationUtil.notNull(userAuthenticationDTO);

        UserAuthentication userAuthentication = UserAuthenticationConverter.convertToEntityUserAuthentication(userAuthenticationDTO);
        userAuthentication.setAvailability(true);

        if (userAuthenticationDTO.getAnnouncement() != null) {
            userAuthentication.setInternshipAnnouncements(singletonList(internshipAnnouncementRepository.findOne(userAuthenticationDTO.getAnnouncement())));
        }

        if (userAuthenticationDTO.getRole() != null) {
            userAuthentication.setRole(roleRepository.findByRole(userAuthenticationDTO.getRole()));
        }

        return userAuthentication;
    }

    /**
     * Method returns a list of available users
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAuthenticationDTO> getAvailableUsers() {

        List<Account> accounts = accountRepository.findAllByActive(true);

        return accounts.stream()
                .map(UserAuthenticationConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Method returns a list of all users
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAuthenticationDTO> getUsers() {

        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(UserAuthenticationConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Method returns a page of all available users
     *
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAuthenticationDTO> getAvailableUsers(Pageable pageable) {
        ValidationUtil.notNull(pageable);

        return UserAuthenticationConverter.convertToDTOPage(
                accountRepository.findAllByActive(true, pageable));
    }

    /**
     * Method returns a page of all users from a announcement
     *
     * @param announcementId
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAuthenticationDTO> getUsersByAnnouncement(Long announcementId, Pageable pageable) {
        ValidationUtil.notNull(announcementId);
        ValidationUtil.notNull(pageable);
        ValidationUtil.notNull(userAuthenticationRepository.findAllByInternshipAnnouncementsId(announcementId, pageable));

        List<UserAuthentication> people = userAuthenticationRepository.findAllByInternshipAnnouncementsId(announcementId, pageable);
        List<Account> accounts = new ArrayList<>();

        people.stream()
                .filter(UserAuthentication::isAvailability)
                .forEach(person -> accounts.add(accountRepository.findOne(person.getId())));
        Page<Account> accountPage = new PageImpl<>(accounts);
        return UserAuthenticationConverter.convertToDTOPage(accountPage);
    }

    /**
     * Method returns a user by id
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthenticationDTO getById(Long id) {
        ValidationUtil.notNull(id);
        ValidationUtil.notNull(accountRepository.findOne(id));

        Account account = accountRepository.findOne(id);
        return UserAuthenticationConverter.convertToDTO(account);
    }

    /**
     * Method deletes a user by id
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteUser(final Long id) {
        ValidationUtil.notNull(id);

        Account account = accountRepository.findOne(id);

        ValidationUtil.notNull(account);

        account.setActive(false);
        accountRepository.save(account);

        UserAuthentication person = account.getUserAuthentication();
        person.setAvailability(false);
        userAuthenticationRepository.save(person);
    }

    /**
     * Method returns a user by username
     *
     * @param username
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserAuthenticationDTO findByUsername(String username) {
        ValidationUtil.notNull(username);

        Account account = accountRepository.findByUsername(username);
        ValidationUtil.notNull(account);

        UserAuthentication person = account.getUserAuthentication();

        return UserAuthenticationDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .role(person.getRole().getRole())
                .announcement(CollectionUtils.isEmpty(person.getInternshipAnnouncements()) ? null : person.getInternshipAnnouncements().get(0).getId())
                .skills(person.getSkills())
                .registrationDate(account.getRegistrationDate())
                .availability(person.isAvailability())
                .build();
    }

    /**
     * Method checks if there is already a user with the given email
     *
     * @param email
     * @return
     */
    @Override
    @Transactional
    public boolean checkEmail(String email) {
        ValidationUtil.notNull(email);

        UserAuthentication person = userAuthenticationRepository.findByEmail(email);

        return !ObjectUtils.isEmpty(person);
    }

    /**
     * Method returns a list of users from a given announcement
     *
     * @param announcementId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserAuthenticationDTO> getUsersByAnnouncement(Long announcementId) {
        ValidationUtil.notNull(announcementId);

        List<UserAuthentication> people = userAuthenticationRepository.findAllByInternshipAnnouncementsId(announcementId);
        List<Account> accounts = new ArrayList<>();

        people.stream()
                .filter(UserAuthentication::isAvailability)
                .forEach(person -> accounts.add(accountRepository.findOne(person.getId())));

        return accounts.stream()
                .map(UserAuthenticationConverter::convertToDTO)
                .collect(Collectors.toList());

    }

    /**
     * Method edits the information of a user
     *
     * @param userAuthenticationDTO
     * @return
     */
    @Override
    @Transactional
    public UserAuthenticationDTO editUser(UserAuthenticationDTO userAuthenticationDTO) {
        ValidationUtil.notNull(userAuthenticationDTO);

        Account account = accountRepository.findOne(userAuthenticationDTO.getId());

        ValidationUtil.notNull(account);

        account.setUsername(userAuthenticationDTO.getUsername());
        account.setPassword(userAuthenticationDTO.getPassword());
        UserAuthentication person = account.getUserAuthentication();
        person.setRole(new Role(userAuthenticationDTO.getRole()));
        person.setFirstName(userAuthenticationDTO.getFirstName());
        person.setLastName(userAuthenticationDTO.getLastName());
        person.setEmail(userAuthenticationDTO.getEmail());
        person.setSkills(userAuthenticationDTO.getSkills());
        //person.setAvailability(userAuthenticationDTO.isAvailability());
        person.setContact(userAuthenticationDTO.getContact());
        person.setDomainsOfInterest(userAuthenticationDTO.getDomainsOInterest());
        person.setEducation(userAuthenticationDTO.getEducation());
        person.setVolunteerExperience(userAuthenticationDTO.getVolunteerExperience());
        person.setHobbies(userAuthenticationDTO.getHobbies());
        person.setWorkHistory(userAuthenticationDTO.getWorkHistory());
        person.setAvailability(true);

        accountRepository.save(account);
        userAuthenticationRepository.save(person);

        return userAuthenticationDTO;
    }

    /**
     * Method updates an account
     *
     * @param userAuthenticationDTO
     * @return
     */
    @Override
    @Transactional
    public UserAuthenticationDTO updateAccount(UserAuthenticationDTO userAuthenticationDTO) {
        ValidationUtil.notNull(userAuthenticationDTO);

        Account account = accountRepository.findOne(userAuthenticationDTO.getId());

        ValidationUtil.notNull(account);
        account.setUsername(userAuthenticationDTO.getUsername());
        account.setPassword(userAuthenticationDTO.getPassword());

        UserAuthentication person = account.getUserAuthentication();

        person.setFirstName(userAuthenticationDTO.getFirstName());
        person.setLastName(userAuthenticationDTO.getLastName());
        person.setEmail(userAuthenticationDTO.getEmail());
        person.setSkills(userAuthenticationDTO.getSkills());
        //person.setAvailability(userAuthenticationDTO.isAvailability());
        person.setContact(userAuthenticationDTO.getContact());
        person.setDomainsOfInterest(userAuthenticationDTO.getDomainsOInterest());
        person.setEducation(userAuthenticationDTO.getEducation());
        person.setVolunteerExperience(userAuthenticationDTO.getVolunteerExperience());
        person.setHobbies(userAuthenticationDTO.getHobbies());
        person.setWorkHistory(userAuthenticationDTO.getWorkHistory());
        person.setAvailability(true);

        accountRepository.save(account);
        userAuthenticationRepository.save(person);

        return userAuthenticationDTO;
    }

    /**
     * Method checks if there is already a user with the given username
     *
     * @param username
     * @return
     */
    @Override
    @Transactional
    public boolean checkUsername(String username) {
        ValidationUtil.notNull(username);

        Account account = accountRepository.findByUsername(username);

        return !ObjectUtils.isEmpty(account);
    }
}
