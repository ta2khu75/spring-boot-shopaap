package dev.ta2khu75.shopapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ta2khu75.shopapp.models.Role;

@Repository
public interface RepositoryRole extends JpaRepository<Role, Integer> {
}
