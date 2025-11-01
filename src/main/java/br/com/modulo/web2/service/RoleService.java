package br.com.modulo.web2.service;

import br.com.modulo.web2.entity.Role;

import java.util.Optional;


public interface RoleService {
    Optional<Role> findByName(Role.RoleName name);
}
