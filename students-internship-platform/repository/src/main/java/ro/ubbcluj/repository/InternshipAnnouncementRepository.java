package ro.ubbcluj.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.ubbcluj.entity.InternshipAnnouncement;

import java.util.Date;
import java.util.List;

public interface InternshipAnnouncementRepository extends JpaRepository<InternshipAnnouncement, Long> {

    Page<InternshipAnnouncement> findByTitle(String title, Pageable pageable);

    Page<InternshipAnnouncement> findAllByAvailability(boolean availability, Pageable pageable);

    List<InternshipAnnouncement> findAllByAvailability(boolean availability);

    Page<InternshipAnnouncement> findByAvailabilityAndUserAuthenticationAccountUsername(boolean b, String username, Pageable pageable);

    InternshipAnnouncement findByUserAuthenticationAccountUsername(String username);

    //    @Query(value = "SELECT * FROM internship_announcement WHERE location = ?1 or posting_date = ?2 or start_date = ?3 or end_date = ?4 or domains = ?5 or paid_or_not = ?6 or duration ?7 or working_time = ?8  ORDER BY id \\n-- #pageable\\n",
//            countQuery = "SELECT count(*) FROM internship_announcement WHERE location = ?1 or posting_date = ?2 or start_date = ?3 or end_date = ?4 or domains = ?5 or paid_or_not = ?6 or duration ?7 or working_time = ?8",
//            nativeQuery = true)
//    Page<InternshipAnnouncement> findAllByLocationOrPostingDateOrStartDateOrEndDateOrDomainsOrPaidOrNotOrDurationOrWorkingTime(String location,
//                                                             Date postingDate,
//                                                             Date startingDate,
//                                                             Date endDate,
//                                                             String domains,
//                                                             boolean paidOrNot,
//                                                             String duration,
//                                                             String workingTime,
//                                                             Pageable pageable);
//    @Query(value = "SELECT * FROM internship_announcement WHERE location = ?1 or posting_date = ?2 or start_date = ?3 or end_date = ?4 or domains = ?5 or paid_or_not = ?6 or duration ?7 or working_time = ?8",
////            " ORDER BY id \\n-- #pageable\\n",
////            countQuery = "SELECT count(*) FROM internship_announcement WHERE location = ?1 or posting_date = ?2 or start_date = ?3 or end_date = ?4 or domains = ?5 or paid_or_not = ?6 or duration ?7 or working_time = ?8",
//            nativeQuery = true)
//    List<InternshipAnnouncement> findAllByLocationOrPostingDateOrStartDateOrEndDateOrDomainsOrPaidOrNotOrDurationOrWorkingTime(String location,
//                                                                                                                               Date postingDate,
//                                                                                                                               Date startingDate,
//                                                                                                                               Date endDate,
//                                                                                                                               String domains,
//                                                                                                                               boolean paidOrNot,
//                                                                                                                               String duration,
//                                                                                                                               String workingTime);
}
