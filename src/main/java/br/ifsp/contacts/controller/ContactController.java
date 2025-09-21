package br.ifsp.contacts.controller;

import br.ifsp.contacts.dto.ContactDTO;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.repository.ContactRepository;
import br.ifsp.contacts.exception.ResourceNotFoundException;
import br.ifsp.contacts.mapper.ContactMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
@Validated
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    @GetMapping
    public List<ContactDTO> getAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        return contactMapper.toDTOList(contacts);
    }

    @GetMapping("{id}")
    public ContactDTO getContactById(@PathVariable Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + id));
        return contactMapper.toDTO(contact);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO createContact(@Valid @RequestBody ContactDTO contactDTO) {
        Contact contact = contactMapper.toEntity(contactDTO);
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toDTO(savedContact);
    }

    @PutMapping("/{id}")
    public ContactDTO updateContact(@PathVariable Long id, @Valid @RequestBody ContactDTO updatedContactDTO) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + id));

        existingContact.setNome(updatedContactDTO.getNome());
        existingContact.setEmail(updatedContactDTO.getEmail());
        existingContact.setTelefone(updatedContactDTO.getTelefone());
        
        // Converter DTOs de endereços para entidades
        if (updatedContactDTO.getAddresses() != null) {
            existingContact.setAddresses(updatedContactDTO.getAddresses().stream()
                    .map(addressDTO -> {
                        Address address = contactMapper.addressToEntity(addressDTO);
                        address.setContact(existingContact);
                        return address;
                    })
                    .collect(java.util.stream.Collectors.toList()));
        }

        Contact savedContact = contactRepository.save(existingContact);
        return contactMapper.toDTO(savedContact);
    }

    @PatchMapping("/{id}")
    public ContactDTO updateContactPartial(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "nome":
                    contact.setNome(value);
                    break;
                case "telefone":
                    contact.setTelefone(value);
                    break;
                case "email":
                    contact.setEmail(value);
                    break;
            }
        });

        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toDTO(savedContact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }

    @GetMapping("/search")
    public List<ContactDTO> searchContactsByName(@RequestParam String name) {
        List<Contact> contacts = contactRepository.findByNomeContainingIgnoreCase(name);
        return contactMapper.toDTOList(contacts);
    }
}