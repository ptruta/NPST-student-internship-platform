package ro.ubbcluj.repository;

import ro.ubbcluj.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByAnnouncements_id(Long announcementId);

    Page<Person> findAllByAnnouncements_id(Long announcementId, Pageable pageable);

    Person findByAccount_Username(String username);

    Person findByEmail(String email);

}
