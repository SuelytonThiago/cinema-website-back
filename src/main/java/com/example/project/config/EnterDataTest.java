//package com.example.project.config;
//
//import com.example.project.domain.entities.Roles;
//import com.example.project.domain.entities.Users;
//import com.example.project.domain.repositories.RoleRepository;
//import com.example.project.domain.repositories.UsersRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Arrays;
//
//@Configuration
//public class EnterDataTest implements CommandLineRunner {
//
//    @Autowired
//    private UsersRepository usersRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder encoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        Users users = new Users();
//        users.setName("adm");
//        users.setEmail("adm@example.com");
//        users.setContactNumber("99940028922");''
//        users.setCpf("61254591010");
//        users.setPassword(encoder.encode("senha123"));
//        users.setProfileImg("https://cdn-icons-png.flaticon.com/512/3106/3106921.png");
//        usersRepository.save(users);
//
//        Users users1 = new Users();
//
//        users1.setName("maria");
//        users1.setEmail("maria@example.com");
//        users1.setContactNumber("99940028922");
//        users1.setCpf("87466407030");
//        users1.setPassword(encoder.encode("senha123"));
//        users1.setProfileImg("https://i0.wp.com/newdoorfiji.com/wp-content/uploads/2018/03/profile-img-1.jpg?ssl=1");
//        usersRepository.save(users1);
//
//
//        Roles roles = new Roles(null,"ROLE_ADMIN");
//        Roles roles1 = new Roles(null,"ROLE_USER");
//        Roles roles2 = new Roles(null,"ROLE_OWNER");
//        roleRepository.saveAll(Arrays.asList(roles,roles1,roles2));
//
//        users.getRoles().add(roles);
//        users1.getRoles().add(roles1);
//        usersRepository.saveAll(Arrays.asList(users,users1));
//    }
//}
