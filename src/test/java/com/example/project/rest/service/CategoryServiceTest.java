package com.example.project.rest.service;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.entities.Roles;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.CategoryRepository;
import com.example.project.rest.dto.CategoryRequestDto;
import com.example.project.rest.dto.CategoryResponseDto;
import com.example.project.rest.services.CategoryService;
import com.example.project.rest.services.UsersService;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private  CategoryRepository repository;
    @Mock
    private  MessageSource messageSource;
    @Mock
    private  UsersService usersService;
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private PasswordEncoder encoder;

    private Categories category;
    private CategoryRequestDto categoryRequestDto;
    private CategoryResponseDto categoryResponseDto;
    private Users adm;
    private Users user;
    private Roles admRole;
    private Roles userRole;

    @BeforeEach
    public void setUp() {
        category = new Categories(1L, "suspense");
        categoryRequestDto = new CategoryRequestDto("ação");

        adm = new Users();
        user = new Users();

        adm.setName("adm");
        adm.setEmail("adm@example.com");
        adm.setContactNumber("99940028922");
        adm.setCpf("61254591010");
        adm.setPassword(encoder.encode("Senha123"));
        adm.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.");

        user.setName("maria");
        user.setEmail("maria@example.com");
        user.setContactNumber("99940028922");
        user.setCpf("87466407030");
        user.setPassword(encoder.encode("Senha123"));
        user.setProfileImg("https://minhas-imagens-2025.s3.sa-east-1.amazonaws.com/user.jpg");

        admRole = new Roles(null,"ROLE_ADMIN");
        userRole = new Roles(null,"ROLE_USER");

        user.getRoles().add(userRole);
        adm.getRoles().add(admRole);
    }

    @Test
    void testCreateNewCategory(){
        HttpServletRequest request = mock(HttpServletRequest.class);

        categoryService.addNewCategory(categoryRequestDto,request);

        verify(usersService).checkIfIsADM(any(HttpServletRequest.class));
        verify(repository).save(any(Categories.class));
        verify(repository).findByName(anyString());
        verifyNoMoreInteractions(usersService);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindByName() {
        given(repository.findByName(anyString())).willReturn(Optional.of(category));

        var response = categoryService.findByName(category.getName());

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(category);
        verify(repository).findByName(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindByNameWithInvalidName() {
        var name = "asdasdasd";
        given(repository.findByName(anyString())).willReturn(Optional.empty());
        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> categoryService.findByName(name))
                .withMessage(
                        messageSource.getMessage("category.service.error.notFound", null, LocaleContextHolder.getLocale())
                );

        verify(repository).findByName(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindLikeName() {
        given(repository.findByNameLike(anyString())).willReturn(Collections.singletonList(category));

        var list = categoryService.findLikeName(category.getName());
        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("name")
                .containsExactly(
                        category.getName()
                );
        verify(repository).findByNameLike(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindLikeNameIfReturnEmptyList(){
        var name = "asdasdasdasd";
        given(repository.findByNameLike(anyString())).willReturn(Collections.emptyList());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> categoryService.findLikeName(name))
                .withMessage(
                        messageSource.getMessage("category.findLike.service.error.notFound", null, LocaleContextHolder.getLocale())
                );

        verify(repository).findByNameLike(anyString());
        verifyNoMoreInteractions(repository);


    }

    @Test
    void testFindAllCategories() {
        given(repository.findAll()).willReturn(Collections.singletonList(category));

        var list = categoryService.findAllCategories();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list).extracting("name")
                .containsExactly(
                        category.getName()
                );
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }


    @Test
    void testFindAllCategoriesIfReturnEmptyList() {
        given(repository.findAll()).willReturn(Collections.emptyList());

        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> categoryService.findAllCategories())
                .withMessage(
                        messageSource.getMessage("category.service.error.emptyList", null, LocaleContextHolder.getLocale()));

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindById() {
        given(repository.findById(anyLong())).willReturn(Optional.of(category));

        var response = categoryService.findById(category.getId());

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(category);
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testFindByIdWithInvalidId() {
        var id = 5L;
        given(repository.findById(anyLong())).willReturn(Optional.empty());
        assertThatExceptionOfType(ObjectNotFoundExceptions.class)
                .isThrownBy(() -> categoryService.findById(id))
                .withMessage(
                        messageSource.getMessage("category.service.error.notFound", null, LocaleContextHolder.getLocale()));

        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }
}
