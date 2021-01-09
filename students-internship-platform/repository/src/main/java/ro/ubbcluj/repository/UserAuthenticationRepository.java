package ro.ubbcluj.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.ubbcluj.entity.UserAuthentication;

import java.util.List;

public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication,Long> {

    UserAuthentication findByEmail(String email);

    UserAuthentication findByAccountUsername(String username);

    List<UserAuthentication> findAllByInternshipAnnouncementsId(Long announcementID, Pageable pageable);

    List<UserAuthentication> findAllByInternshipAnnouncementsId(Long announcementID);
}
