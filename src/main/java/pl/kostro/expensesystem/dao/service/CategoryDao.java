package pl.kostro.expensesystem.dao.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostro.expensesystem.dao.model.CategoryEntity;
import pl.kostro.expensesystem.dao.repository.CategoryRepository;
import pl.kostro.expensesystem.dto.model.Category;
import pl.kostro.expensesystem.utils.Converter;

@Service
public class CategoryDao {
  
  @Autowired
  private CategoryRepository cr;

  public void save(Category category) {
    CategoryEntity categoryEntity = Converter.toCategoryEntity(category);
    cr.save(categoryEntity);
    category.setId(categoryEntity.getId());
  }

  public void merge(Category category) {
    CategoryEntity categoryEntity = cr.findOne(category.getId());
    Converter.toCategoryEntity(category, categoryEntity);
    cr.save(categoryEntity);
  }

  public void delete(Category category) {
    cr.delete(category.getId());
  }

}
