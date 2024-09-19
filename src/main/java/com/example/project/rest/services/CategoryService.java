package com.example.project.rest.services;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.repositories.CategoryRepository;
import com.example.project.rest.dto.CategoryRequestDto;
import com.example.project.rest.services.exceptions.CustomException;
import com.example.project.rest.services.exceptions.ObjectNotFoundExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public void addNewCategory(CategoryRequestDto dto){
        verifyIfExist(dto.getName());
        repository.save(new Categories(dto.getName()));
    }

    public Categories findByName(String name){
        return repository.findByName(name).orElseThrow(
                () -> new ObjectNotFoundExceptions("this category is not found"));
    }

    public List<Categories> findAllCategories(){
        return repository.findAll();
    }

    public Categories findById(Long id){
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundExceptions("this category is not found"));
    }

    public void verifyIfExist(String name){
        repository.findByName(name).ifPresent((e) -> {
            throw new CustomException("this category Already exist");
        });
    }
}
