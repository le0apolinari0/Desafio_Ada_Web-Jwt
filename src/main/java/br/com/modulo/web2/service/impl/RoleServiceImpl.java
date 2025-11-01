package br.com.modulo.web2.service.impl;

import br.com.modulo.web2.entity.Role;
import br.com.modulo.web2.repository.RoleRepository;
import br.com.modulo.web2.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Cacheable(value = "roles", key = "#name")
    public Optional<Role> findByName(Role.RoleName name) {
        return roleRepository.findByName(name);
    }
}
