package com.ecom.serviceimpl;

import com.ecom.entity.Category;
import com.ecom.repository.CategoryRepository;
import com.ecom.service.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl  implements CategoryServices {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public String createCategory(Category category) {
        String id = UUID.randomUUID().toString();
        category.setId(id);

        //set cerated date as current epock time
        category.setCreatedDate(System.currentTimeMillis());
        categoryRepository.save(category);
return "Category created with ID"+id;
    }
}

//get category by id
//get all category
//update category
//delete category







