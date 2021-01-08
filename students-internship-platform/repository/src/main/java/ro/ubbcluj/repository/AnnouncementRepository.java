package ro.ubbcluj.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.ubbcluj.entity.Announcement;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    Page<Announcement> findByTitle(String title, Pageable pageable);

    Page<Announcement> findAllByActive(boolean active, Pageable pageable);

    List<Announcement> findAllByActive(boolean active);

    Page<Announcement> findByActiveAndPerson_Account_Username(boolean active, String username, Pageable pageable);

    Announcement findByPerson_Account_Username(String username);

}
