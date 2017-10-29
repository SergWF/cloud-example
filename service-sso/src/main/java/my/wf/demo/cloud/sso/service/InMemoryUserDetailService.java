package my.wf.demo.cloud.sso.service;

import my.wf.demo.cloud.sso.model.UserInfo;
import my.wf.demo.cloud.sso.model.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Qualifier("userDetailsService")
public class InMemoryUserDetailService implements UserDetailsService {

    private static final List<UserDetails> users = new CopyOnWriteArrayList<>();

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void loadUsers(){
        users.add(UserInfo.builder()
                          .username("user1")
                          .password(passwordEncoder.encode("123"))
                          .authority(new SimpleGrantedAuthority(Roles.USER.name()))
                          .someData("SOME ADDITIONAL DATA FOR USER1")
                          .build());
        users.add(UserInfo.builder()
                          .username("user2")
                          .password(passwordEncoder.encode("123"))
                          .authority(new SimpleGrantedAuthority(Roles.USER.name()))
                          .someData("SOME ADDITIONAL DATA FOR USER2")
                          .build());
        users.add(UserInfo.builder()
                          .username("admin")
                          .password(passwordEncoder.encode("123"))
                          .authority(new SimpleGrantedAuthority(Roles.ADMIN.name()))
                          .someData("SOME ADDITIONAL DATA FOR ADMIN")
                          .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                    .filter(u -> u.getUsername().toLowerCase().equals(username.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
