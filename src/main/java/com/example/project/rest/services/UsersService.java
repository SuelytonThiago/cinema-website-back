package com.example.project.rest.services;

import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.UsersRepository;
import com.example.project.rest.dto.UserRequestDto;
import com.example.project.rest.services.exceptions.AlreadyExistsExceptions;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public void findFirstUserByEmailOrCpf(String email,String cpf){
        usersRepository.findFirstByEmailOrCpf(email,cpf).ifPresent( e -> {
            throw new AlreadyExistsExceptions("This email or cpf already in use");
        });
    }

    public void createNewUser(UserRequestDto userDto){
        findFirstUserByEmailOrCpf(userDto.getEmail(),userDto.getCpf());
        var user = Users.of(userDto);

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
    public void updateUserData(UserRequestDto dto, Long id){
        var user = findById(id);
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
        newUser.setPassword(dto.getPassword());
    }


}
