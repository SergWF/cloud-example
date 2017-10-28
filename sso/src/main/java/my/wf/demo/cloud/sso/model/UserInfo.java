package my.wf.demo.cloud.sso.model;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Getter
@Builder
@ToString
public class UserInfo implements UserDetails {
    @Singular
    private Collection<? extends GrantedAuthority> authorities;
    private String password;
    private String username;
    @Default
    private boolean accountNonExpired = true;
    @Default
    private boolean accountNonLocked = true;
    @Default
    private boolean credentialsNonExpired = true;
    @Default
    private boolean enabled = true;
    private String someData;

}
