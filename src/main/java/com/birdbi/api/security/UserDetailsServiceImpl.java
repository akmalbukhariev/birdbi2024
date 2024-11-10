package com.birdbi.api.security;

import com.birdbi.api.CamelCaseMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CamelCaseMap map = new CamelCaseMap();

        return createUserDetails(map);
    }

    private UserDetails createUserDetails(CamelCaseMap map) {
        UserDetails user = User.builder()
                .username((String)map.get("userId"))
                .password((String)map.get("passwd"))
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        return new CommUserDetails(user, map);
    }

    public UserDetails setting(String userId) throws Exception {

        CamelCaseMap map = new CamelCaseMap();
        return createUserDetails(map);
    }
}
