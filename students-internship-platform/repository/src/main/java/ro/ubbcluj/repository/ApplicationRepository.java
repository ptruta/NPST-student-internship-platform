package ro.ubbcluj.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.ubbcluj.entity.Application;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByPerson_Account_Username(String username);

    List<Application> findAllByAnnouncement_Id(Long announcementId);

    Page<Application> findAllByAnnouncement_Id(Long announcementId, Pageable pageable);

    Page<Application> findByAnnouncement_Person_Account_Username(String username, Pageable pageable);
}
