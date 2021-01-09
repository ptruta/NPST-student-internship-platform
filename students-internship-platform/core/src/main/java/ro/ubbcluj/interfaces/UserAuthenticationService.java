package ro.ubbcluj.interfaces;

import ro.ubbcluj.dto.UserAuthenticationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserAuthenticationService {

    UserAuthenticationDTO createUser(UserAuthenticationDTO userAuthenticationDTO);

    List<UserAuthenticationDTO> getUsers();

    List<UserAuthenticationDTO> getAvailableUsers();

    UserAuthenticationDTO getById(Long id);

    void deleteUser(Long id);

    UserAuthenticationDTO findByUsername(String username);

    UserAuthenticationDTO editUser(UserAuthenticationDTO userAuthenticationDTO);

    List<UserAuthenticationDTO> getUsersByAnnouncement(Long announcementId);

    UserAuthenticationDTO updateAccount(UserAuthenticationDTO userAuthenticationDTO);

    boolean checkUsername(String username);

    boolean checkEmail(String email);

    Page<UserAuthenticationDTO> getAvailableUsers(Pageable pageable);

    Page<UserAuthenticationDTO> getUsersByAnnouncement(Long announcementId, Pageable pageable);
}
