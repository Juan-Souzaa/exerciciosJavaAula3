package br.ifsp.contacts.controller;

import br.ifsp.contacts.dto.AddressDTO;
import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.repository.AddressRepository;
import br.ifsp.contacts.repository.ContactRepository;
import br.ifsp.contacts.exception.ResourceNotFoundException;
import br.ifsp.contacts.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;
    
    @GetMapping("/contacts/{contactId}")
    public Page<AddressDTO> getAddressesByContact(@PathVariable Long contactId, 
                                                 @PageableDefault(size = 10, sort = "rua") Pageable pageable) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + contactId));
        
        Page<Address> addresses = addressRepository.findByContactId(contactId, pageable);
        return addresses.map(addressMapper::toDTO);
    }
    
    @PostMapping("/contacts/{contactId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO createAddress(@PathVariable Long contactId, @RequestBody @Valid AddressDTO addressDTO) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + contactId));
        
        Address address = addressMapper.toEntity(addressDTO);
        address.setContact(contact);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDTO(savedAddress);
    }
    
    @GetMapping("/search/cidade")
    public Page<AddressDTO> searchAddressesByCity(@RequestParam String cidade, 
                                                 @PageableDefault(size = 10, sort = "rua") Pageable pageable) {
        Page<Address> addresses = addressRepository.findByCidadeContainingIgnoreCase(cidade, pageable);
        return addresses.map(addressMapper::toDTO);
    }
    
    @GetMapping("/search/estado")
    public Page<AddressDTO> searchAddressesByState(@RequestParam String estado, 
                                                  @PageableDefault(size = 10, sort = "cidade") Pageable pageable) {
        Page<Address> addresses = addressRepository.findByEstadoContainingIgnoreCase(estado, pageable);
        return addresses.map(addressMapper::toDTO);
    }
    
    @GetMapping("/search/cep")
    public Page<AddressDTO> searchAddressesByCep(@RequestParam String cep, 
                                                @PageableDefault(size = 10, sort = "rua") Pageable pageable) {
        Page<Address> addresses = addressRepository.findByCepContaining(cep, pageable);
        return addresses.map(addressMapper::toDTO);
    }
}
