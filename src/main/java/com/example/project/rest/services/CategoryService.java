package com.example.project.rest.services;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.repositories.CategoryRepository;
import com.example.project.rest.dto.CategoryRequestDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
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

    public void addNewCategory(CategoryRequestDto dto){
        verifyIfExist(dto.getName());
        repository.save(new Categories(dto.getName()));
    }

    public Categories findByName(String name){
        return repository.findByName(name).orElseThrow(
                () -> new ObjectNotFoundExceptions(
                        messageSource.getMessage("category.service.error.notFound", null, LocaleContextHolder.getLocale())
                ));
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
