package br.ifsp.contacts.mapper;

import br.ifsp.contacts.dto.AddressDTO;
import br.ifsp.contacts.model.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressMapper {

    public AddressDTO toDTO(Address address) {
        if (address == null) {
            return null;
        }

        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setRua(address.getRua());
        dto.setCidade(address.getCidade());
        dto.setEstado(address.getEstado());
        dto.setCep(address.getCep());
        
        // Não incluir o contato para evitar serialização cíclica
        // O contato será incluído apenas quando necessário

        return dto;
    }

    public Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }

        Address address = new Address();
        address.setRua(dto.getRua());
        address.setCidade(dto.getCidade());
        address.setEstado(dto.getEstado());
        address.setCep(dto.getCep());

        return address;
    }

    public List<AddressDTO> toDTOList(List<Address> addresses) {
        if (addresses == null) {
            return null;
        }

        return addresses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
