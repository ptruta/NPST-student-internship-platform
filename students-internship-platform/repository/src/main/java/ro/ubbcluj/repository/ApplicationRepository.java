package ro.ubbcluj.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.ubbcluj.entity.Application;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByUserAuthenticationAccountUsername(String username);

    List<Application> findAllByInternshipAnnouncementId(Long announcementId);

    Page<Application> findAllByInternshipAnnouncementId(Long announcementId, Pageable pageable);

    Page<Application> findByInternshipAnnouncementUserAuthenticationAccountUsername(String username, Pageable pageable);
}
