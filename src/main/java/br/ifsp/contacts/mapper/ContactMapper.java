package br.ifsp.contacts.mapper;

import br.ifsp.contacts.dto.ContactDTO;
import br.ifsp.contacts.dto.AddressDTO;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.model.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactMapper {

    public ContactDTO toDTO(Contact contact) {
        if (contact == null) {
            return null;
        }

        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setNome(contact.getNome());
        dto.setEmail(contact.getEmail());
        dto.setTelefone(contact.getTelefone());
        
        if (contact.getAddresses() != null) {
            dto.setAddresses(contact.getAddresses().stream()
                    .map(this::addressToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Contact toEntity(ContactDTO dto) {
        if (dto == null) {
            return null;
        }

        Contact contact = new Contact();
        contact.setNome(dto.getNome());
        contact.setEmail(dto.getEmail());
        contact.setTelefone(dto.getTelefone());
        
        if (dto.getAddresses() != null) {
            contact.setAddresses(dto.getAddresses().stream()
                    .map(this::addressToEntity)
                    .collect(Collectors.toList()));
        }

        return contact;
    }

    public List<ContactDTO> toDTOList(List<Contact> contacts) {
        if (contacts == null) {
            return null;
        }

        return contacts.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AddressDTO addressToDTO(Address address) {
        if (address == null) {
            return null;
        }

        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setRua(address.getRua());
        dto.setCidade(address.getCidade());
        dto.setEstado(address.getEstado());
        dto.setCep(address.getCep());
        

        return dto;
    }

    public Address addressToEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }

        Address address = new Address();
        address.setId(dto.getId());
        address.setRua(dto.getRua());
        address.setCidade(dto.getCidade());
        address.setEstado(dto.getEstado());
        address.setCep(dto.getCep());

        return address;
    }
}
