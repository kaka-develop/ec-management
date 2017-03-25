package org.group2.webapp.service;

import org.group2.webapp.entity.Authority;
import org.group2.webapp.entity.User;
import org.group2.webapp.repository.AuthorityRepository;
import org.group2.webapp.repository.UserRepository;
import org.group2.webapp.security.AuthoritiesConstants;
import org.group2.webapp.security.SecurityUtils;
import org.group2.webapp.service.dto.UserDTO;
import org.group2.webapp.util.RandomUtil;
import org.group2.webapp.web.mvc.vm.UserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private static final String ANONYMOUS_USER = "anonymoususer";
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public User createUser(String username, String password, String firstName, String lastName, String email) {
        if (userRepository.findOneWithAuthoritiesByUsername(username).isPresent())
            return null;
        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.STUDENT);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        System.out.println(newUser.getCreatedDate());
        userRepository.save(newUser);
        return newUser;
    }


    public User createUser(UserDTO userDTO) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDTO.getUsername()).isPresent())
            return null;
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userDTO.getAuthorities().forEach(
                    authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }

        if (userDTO instanceof UserVM) {
            UserVM userVM = (UserVM) userDTO;
            user.setPassword(passwordEncoder.encode(userVM.getPassword()));
        } else {
            String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
            user.setPassword(encryptedPassword);
        }
        userRepository.save(user);
        return user;
    }

    public void updateUser(String firstName, String lastName, String email) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
        });
    }


    public void deleteUser(String username) {
        if (username == null)
            return;
        userRepository.findOneByUsername(username).ifPresent(user -> {
            userRepository.delete(user);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
        });
    }

    @Transactional
    public List<UserDTO> getAllManagedUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public Optional<User> getUserWithAuthoritiesByUsername(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional
    public User getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByUsername(SecurityUtils.getCurrentUserLogin()).orElse(null);
    }


    public User updateUser(UserVM userVM) {
        if (!userRepository.findOneByUsername(userVM.getUsername()).isPresent())
            return null;
        User user = userRepository.findOneByUsername(userVM.getUsername()).get();
        user.setFirstName(userVM.getFirstName());
        user.setLastName(userVM.getLastName());
        user.setEmail(userVM.getEmail());
        if (userVM.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userVM.getAuthorities().forEach(
                    authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        user = userRepository.save(user);
        return user;
    }

    public UserDTO findOneByUsername(String username) {
        if (!userRepository.findOneByUsername(username).isPresent())
            return null;
        UserDTO userDTO = new UserDTO(userRepository.findOneByUsername(username).get());
        return userDTO;
    }

}
