package br.com.modulo.web2.mapper;


import br.com.modulo.web2.dto.request.UserRequest;
import br.com.modulo.web2.dto.response.UserResponse;
import br.com.modulo.web2.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface UserMapper {
    @Mappings({
            @Mapping(target = "enderecos", source = "addresses"),
            @Mapping(target = "roles", expression = "java(usuario.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))")
    })
    UserResponse toResponse(Usuario usuario);

    @Mappings({
            @Mapping(target = "nome", source = "nome"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "cpf", source = "cpf"),
            @Mapping(target = "telefone", source = "telefone"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "addresses", source = "enderecos")
    })
    Usuario toEntity(UserRequest userRequest);
}
