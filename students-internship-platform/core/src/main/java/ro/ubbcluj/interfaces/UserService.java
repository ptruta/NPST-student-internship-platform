package ro.ubbcluj.interfaces;

import ro.ubbcluj.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getUsers();

    List<UserDTO> getAvailableUsers();

    UserDTO getById(Long id);

    void deleteUser(Long id);

    UserDTO findByUsername(String username);

    UserDTO editUser(UserDTO userDTO);

    List<UserDTO> getUsersByAnnouncement(Long announcementId);

    UserDTO updateAccount(UserDTO userDTO);

    boolean checkUsername(String username);

    boolean checkEmail(String email);

    Page<UserDTO> getAvailableUsers(Pageable pageable);

    Page<UserDTO> getUsersByAnnouncement(Long announcementId, Pageable pageable);
}
