package dev.ta2khu75.shopapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.Category;

@Repository
public interface RepositoryCategory extends JpaRepository<Category, Long> {
}
