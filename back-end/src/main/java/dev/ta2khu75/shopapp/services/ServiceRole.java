package dev.ta2khu75.shopapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.ta2khu75.shopapp.models.Role;
import dev.ta2khu75.shopapp.repositories.RepositoryRole;
import dev.ta2khu75.shopapp.services.iservices.IServiceRole;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ServiceRole implements IServiceRole{
  private final RepositoryRole repositoryRole;
  @Override
  public List<Role> getAllRole() {
    // TODO Auto-generated method stub
    return repositoryRole.findAll();
  }

}
