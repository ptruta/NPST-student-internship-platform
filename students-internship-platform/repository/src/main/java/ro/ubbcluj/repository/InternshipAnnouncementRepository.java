package ro.ubbcluj.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.ubbcluj.entity.InternshipAnnouncement;

import javax.net.ssl.SSLSession;
import java.util.Date;
import java.util.List;

public interface InternshipAnnouncementRepository extends JpaRepository<InternshipAnnouncement, Long> {

    Page<InternshipAnnouncement> findByTitle(String title, Pageable pageable);

    Page<InternshipAnnouncement> findAllByAvailability(boolean availability, Pageable pageable);

    List<InternshipAnnouncement> findAllByAvailability(boolean availability);

    Page<InternshipAnnouncement> findByAvailabilityAndUserAuthenticationAccountUsername(boolean b, String username, Pageable pageable);

    InternshipAnnouncement findByUserAuthenticationAccountUsername(String username);
}
