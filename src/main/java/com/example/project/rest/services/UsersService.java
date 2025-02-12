package com.example.project.rest.services;

import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.dto.UserRequestDto;
import com.example.project.rest.dto.UserResponseDto;
import com.example.project.rest.dto.UserUpdateRequestDto;
import com.example.project.rest.services.exceptions.AlreadyExistsExceptions;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.NotAuthenticatedException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsersService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final MessageSource messageSource;

    public void findFirstUserByEmailOrCpf(String email,String cpf){
        usersRepository.findFirstByEmailOrCpf(email,cpf).ifPresent( e -> {
            throw new AlreadyExistsExceptions(
                    messageSource.getMessage("user.service.error.alreadyInUse", null, LocaleContextHolder.getLocale())
            );
        });
    }

    @Transactional
    public void createNewUser(UserRequestDto userDto){
        findFirstUserByEmailOrCpf(userDto.getEmail(),userDto.getCpf());
        var user = Users.of(userDto);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.jpg");
        user.getRoles().add(roleService.findByName("ROLE_USER"));
        usersRepository.save(user);
    }

    public Users findByEmail(String email){
        return usersRepository.findByEmail(email).orElseThrow(

                () -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("user.service.error.notFound", null, LocaleContextHolder.getLocale())
                )
        );
    }

    public Users findById(Long id){
        return usersRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("user.service.error.notFound", null, LocaleContextHolder.getLocale())
                )
        );
    }

    public UserResponseDto findUserById(HttpServletRequest request){
        var id = jwtService.getClaimId(request);
        return UserResponseDto.of(findById(id));
    }


    @Transactional
    public void updateUserData(UserUpdateRequestDto dto, Users user, String password){
        if(!verifyPasswordToChangeUserData(password, user)){

            throw new CustomException(
                    messageSource.getMessage("user.service.error.incorrectPassword", null, LocaleContextHolder.getLocale())
            );
        }
        updateData(dto,user);
        usersRepository.save(user);
    }

    public Users getUserAuthenticated(String authHead){
        String token;
        if(authHead != null) {
            token = authHead.replace("Bearer ", "");
            var email = jwtService.getSubject(token);
            return findByEmail(email);
        } else {

            throw new NotAuthenticatedException(
                    messageSource.getMessage("user.service.error.notAuthenticated", null, LocaleContextHolder.getLocale())
            );
        }
    }

    public void changePassword(Users user, String password){
        user.setPassword( encoder.encode(password));
        usersRepository.save(user);
    }

    public void createNewPassword(String oldPassword, String newPassword, Users user){
        if(!verifyPasswordToChangeUserData(oldPassword,user)){
            throw new CustomException(
                    messageSource.getMessage("user.service.error.incorrectPassword", null, LocaleContextHolder.getLocale())
            );
        }
        user.setPassword(encoder.encode(newPassword));
        usersRepository.save(user);

    }


    private boolean verifyPasswordToChangeUserData(String password, Users user){
        return encoder.matches(password, user.getPassword());
    }

    private void updateData(UserUpdateRequestDto dto, Users newUser){
        newUser.setName(dto.getName());
        newUser.setContactNumber(dto.getContactNumber());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username)
                .orElseThrow(() -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("user.service.error.notFound", null, LocaleContextHolder.getLocale())
                ));
    }
}
