package com.birdbi.api.security.provider;

import com.birdbi.api.security.CommUserDetails;
import com.birdbi.api.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder psssEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException, UsernameNotFoundException {
        // TODO Auto-generated method stub
        String userId = authentication.getName();
        String passwd 	= (String)authentication.getCredentials();

        //log.debug("userId =>"+userId);
        //log.debug("passwd =>"+passwd);

        CommUserDetails user = (CommUserDetails) userDetailsService.loadUserByUsername(userId);

        if(!psssEncoder.matches(passwd, user.getPassword())) {
            throw new BadCredentialsException("Password is not matched!!");
        }
        try {
            user = (CommUserDetails) userDetailsService.setting(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, passwd, user.getAuthorities());
        user.getDataMap().remove("passwd");
        authToken.setDetails(user.getDataMap());
        return authToken;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
