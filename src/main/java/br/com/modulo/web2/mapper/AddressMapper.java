package br.com.modulo.web2.mapper;

import br.com.modulo.web2.dto.request.AddressRequest;
import br.com.modulo.web2.dto.response.AddressResponse;
import br.com.modulo.web2.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse toResponse(Address address);
    Address toEntity(AddressRequest addressRequest);
}
