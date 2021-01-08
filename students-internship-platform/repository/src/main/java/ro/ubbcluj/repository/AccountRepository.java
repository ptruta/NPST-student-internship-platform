package ro.ubbcluj.repository;

import ro.ubbcluj.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByActive(boolean active);

    Account findByUsername(String username);

    Page<Account> findAllByActive(boolean active, Pageable pageable);
}
