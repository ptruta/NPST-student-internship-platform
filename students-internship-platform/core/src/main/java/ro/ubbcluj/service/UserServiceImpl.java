package ro.ubbcluj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ro.ubbcluj.converter.UserConverter;
import ro.ubbcluj.dto.UserDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.Person;
import ro.ubbcluj.entity.Role;
import ro.ubbcluj.interfaces.UserService;
import ro.ubbcluj.repository.AccountRepository;
import ro.ubbcluj.repository.AnnouncementRepository;
import ro.ubbcluj.repository.PersonRepository;
import ro.ubbcluj.repository.RoleRepository;
import ro.ubbcluj.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

/**
 * This class contains all the business logic
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Method creates a new user DTO
     *
     * @param userDTO
     * @return
     */
    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        ValidationUtil.notNull(userDTO);

        Person person = getPerson(userDTO);

        personRepository.save(person);
        Account account = getAccount(userDTO, person);
        accountRepository.save(account);

        return userDTO;
    }

    /**
     * Method used to return the account of a person
     *
     * @param userDTO
     * @param person
     * @return
     */
    private Account getAccount(UserDTO userDTO, Person person) {
        ValidationUtil.notNull(userDTO);
        ValidationUtil.notNull(person);

        Account account = UserConverter.convertToEntityAccount(userDTO);
        account.setPerson(person);
        account.setActive(true);
        return account;
    }

    /**
     * Method used to extract a person from a user DTO
     *
     * @param userDTO
     * @return
     */
    private Person getPerson(UserDTO userDTO) {
        ValidationUtil.notNull(userDTO);

        Person person = UserConverter.convertToEntityPerson(userDTO);
        person.setActive(true);

        if (userDTO.getAnnouncement() != null) {
            person.setAnnouncements(singletonList(announcementRepository.findOne(userDTO.getAnnouncement())));
        }

        if (userDTO.getRole() != null) {
            person.setRole(roleRepository.findByRole(userDTO.getRole()));
        }

        return person;
    }

    /**
     * Method returns a list of available users
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAvailableUsers() {

        List<Account> accounts = accountRepository.findAllByActive(true);

        return accounts.stream()
                .map(UserConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Method returns a list of all users
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers() {

        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(UserConverter::convertToDTO)
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
    public Page<UserDTO> getAvailableUsers(Pageable pageable) {
        ValidationUtil.notNull(pageable);

        return UserConverter.convertToDTOPage(
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
    public Page<UserDTO> getUsersByAnnouncement(Long announcementId, Pageable pageable) {
        ValidationUtil.notNull(announcementId);
        ValidationUtil.notNull(pageable);
        ValidationUtil.notNull(personRepository.findAllByAnnouncements_id(announcementId, pageable));

        List<Person> people = personRepository.findAllByAnnouncements_id(announcementId);
        List<Account> accounts = new ArrayList<>();

        people.stream()
                .filter(Person::isActive)
                .forEach(person -> accounts.add(accountRepository.findOne(person.getId())));
        Page<Account> accountPage = new PageImpl<>(accounts);
        return UserConverter.convertToDTOPage(accountPage);
    }

    /**
     * Method returns a user by id
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserDTO getById(Long id) {
        ValidationUtil.notNull(id);
        ValidationUtil.notNull(accountRepository.findOne(id));

        Account account = accountRepository.findOne(id);
        return UserConverter.convertToDTO(account);
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

        Person person = account.getPerson();
        person.setActive(false);
        personRepository.save(person);
    }

    /**
     * Method returns a user by username
     *
     * @param username
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public UserDTO findByUsername(String username) {
        ValidationUtil.notNull(username);

        Account account = accountRepository.findByUsername(username);
        ValidationUtil.notNull(account);

        Person person = account.getPerson();

        return UserDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .role(person.getRole().getRole())
                .announcement(CollectionUtils.isEmpty(person.getAnnouncements()) ? null : person.getAnnouncements().get(0).getId())
                .birthDate(person.getBirthDate())
                .gender(person.getGender())
                .address(person.getAddress())
                .skills(person.getSkills())
                .registrationDate(account.getRegistrationDate())
                .active(person.isActive())
                .bio(person.getBio())
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

        Person person = personRepository.findByEmail(email);

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
    public List<UserDTO> getUsersByAnnouncement(Long announcementId) {
        ValidationUtil.notNull(announcementId);

        List<Person> people = personRepository.findAllByAnnouncements_id(announcementId);
        List<Account> accounts = new ArrayList<>();

        people.stream()
                .filter(Person::isActive)
                .forEach(person -> accounts.add(accountRepository.findOne(person.getId())));

        return accounts.stream()
                .map(UserConverter::convertToDTO)
                .collect(Collectors.toList());

    }

    /**
     * Method edits the information of a user
     *
     * @param userDTO
     * @return
     */
    @Override
    @Transactional
    public UserDTO editUser(UserDTO userDTO) {
        ValidationUtil.notNull(userDTO);

        Account account = accountRepository.findOne(userDTO.getId());

        ValidationUtil.notNull(account);

        account.setUsername(userDTO.getUsername());
        Person person = account.getPerson();
        person.setRole(new Role(userDTO.getRole()));

        accountRepository.save(account);
        personRepository.save(person);

        return userDTO;
    }

    /**
     * Method updates an account
     *
     * @param userDTO
     * @return
     */
    @Override
    @Transactional
    public UserDTO updateAccount(UserDTO userDTO) {
        ValidationUtil.notNull(userDTO);

        Account account = accountRepository.findOne(userDTO.getId());

        ValidationUtil.notNull(account);
        account.setUsername(userDTO.getUsername());

        Person person = account.getPerson();

        person.setFirstName(userDTO.getFirstName());
        person.setLastName(userDTO.getLastName());
        person.setEmail(userDTO.getEmail());
        person.setAddress(userDTO.getAddress());

        accountRepository.save(account);
        personRepository.save(person);

        return userDTO;
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
