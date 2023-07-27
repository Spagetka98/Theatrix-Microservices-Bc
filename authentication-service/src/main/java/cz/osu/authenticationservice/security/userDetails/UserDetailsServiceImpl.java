package cz.osu.authenticationservice.security.userDetails;

import cz.osu.authenticationservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import cz.osu.authenticationservice.model.database.AppUser;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null || username.trim().isEmpty()) throw new UsernameNotFoundException("Parameter username in UserDetailsServiceImpl.loadUserByUsername was empty or null !");

        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error(String.format("User with username: %s was not found in the database !",username));
                    throw new UsernameNotFoundException(String.format("User with name: %s in UserDetailsServiceImpl.loadUserByUsername was not found !", username));
                });

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities(authorities)
                .build();
    }
}
