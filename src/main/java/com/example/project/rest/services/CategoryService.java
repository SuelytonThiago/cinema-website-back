package com.example.project.rest.services;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.repositories.CategoryRepository;
import com.example.project.rest.dto.CategoryRequestDto;
import com.example.project.rest.dto.CategoryResponseDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final MessageSource messageSource;
    private final UsersService usersService;

    public void addNewCategory(CategoryRequestDto dto, HttpServletRequest request){
        var user = usersService.findUserById(request);
        if(user.getRoles().stream().noneMatch(role -> "ROLE_ADMIN".equals(role.getNameRole()))){
            throw new CustomException(
                    messageSource.getMessage("server.error.unauthorized", null, LocaleContextHolder.getLocale())
            );
        }
        verifyIfExist(dto.getName());
        repository.save(new Categories(dto.getName()));
    }

    public Categories findByName(String name){
        return repository.findByName(name).orElseThrow(
                () -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("category.service.error.notFound", null, LocaleContextHolder.getLocale())
                ));
    }

    public List<CategoryResponseDto> findLikeName(String name) {
        List<CategoryResponseDto> categories = repository.findByNameLike(name)
                .stream()
                .map(CategoryResponseDto::of)
                .toList();

        if (categories.isEmpty()) {
            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("category.findLike.service.error.notFound", null, LocaleContextHolder.getLocale())
            );
        }

        return categories;
    }


    public List<Categories> findAllCategories(){
        var list =  repository.findAll();
        if(list.isEmpty()) {
            throw new ObjectNotFoundExceptions(
                    messageSource.getMessage("category.service.error.emptyList", null, LocaleContextHolder.getLocale()));
        }
        return list;
    }

    public Categories findById(Long id){
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundExceptions(
                messageSource.getMessage("category.service.error.notFound", null, LocaleContextHolder.getLocale()))
        );
    }

    public void verifyIfExist(String name){
        repository.findByName(name).ifPresent((e) -> {

            throw new CustomException(
                    messageSource.getMessage("category.service.error.alreadyExist", null, LocaleContextHolder.getLocale()));
        });
    }
}
