package com.example.project.rest.services;

import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.dto.UserRequestDto;
import com.example.project.rest.services.exceptions.AlreadyExistsExceptions;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final RoleService roleService;

    public void findFirstUserByEmailOrCpf(String email,String cpf){
        usersRepository.findFirstByEmailOrCpf(email,cpf).ifPresent( e -> {
            throw new AlreadyExistsExceptions("This email or cpf already in use");
        });
    }

    @Transactional
    public void createNewUser(UserRequestDto userDto){
        findFirstUserByEmailOrCpf(userDto.getEmail(),userDto.getCpf());
        var user = Users.of(userDto);
        user.setPassword(encoder.encode(user.getPassword()));
        user.getRoles().add(roleService.findByName("ROLE_USER"));
        usersRepository.save(user);
    }

    public Users findByEmail(String email){
        return usersRepository.findByEmail(email).orElseThrow(
                () -> new ObjectNotFoundExceptions("The user is not found")
        );
    }

    public Users findById(Long id){
        return usersRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundExceptions("The user is not found")
        );
    }


    @Transactional
    public void updateUserData(UserRequestDto dto, HttpServletRequest request){
        var userId = jwtService.getClaimId(request);
        var user = findById(userId);
        verifyCpfAndEmailAlreadyInUse(user, dto.getEmail(), dto.getCpf());
        updateData(dto,user);
        usersRepository.save(user);

    }

    private void verifyCpfAndEmailAlreadyInUse(Users user, String email, String cpf){
        var userVerify = usersRepository.findFirstByEmailOrCpf(email,cpf).get();
        if(!user.getId().equals(userVerify.getId())){
            throw new AlreadyExistsExceptions("This email or cpf already in use");
        }
    }

    private void updateData(UserRequestDto dto, Users newUser){
        newUser.setName(dto.getName());
        newUser.setContactNumber(dto.getContactNumber());
        newUser.setEmail(dto.getEmail());
        newUser.setCpf(dto.getCpf());
        newUser.setPassword(encoder.encode(dto.getPassword()));
    }


}
