package ro.ubbcluj.service;

import ro.ubbcluj.dto.UserAuthenticationDTO;
import ro.ubbcluj.interfaces.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

 /**
 * This class contains all the business logic
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

     /**
      * Method loads a user by username, used for log in
      *
      * @param username
      * @return
      * @throws UsernameNotFoundException
      */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthenticationDTO userAuthenticationDTO = userAuthenticationService.findByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userAuthenticationDTO.getRole().name()));

        return new User(userAuthenticationDTO.getUsername(), userAuthenticationDTO.getPassword(), grantedAuthorities);
    }
}
