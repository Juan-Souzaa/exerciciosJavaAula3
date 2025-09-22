package br.ifsp.contacts.controller;

import br.ifsp.contacts.dto.ContactDTO;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.repository.ContactRepository;
import br.ifsp.contacts.exception.ResourceNotFoundException;
import br.ifsp.contacts.mapper.ContactMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
@Validated
@Tag(name = "Contatos", description = "Operações relacionadas ao gerenciamento de contatos")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Operation(summary = "Listar todos os contatos", description = "Recupera uma lista paginada de todos os contatos")
    @GetMapping
    public Page<ContactDTO> getAllContacts(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<Contact> contacts = contactRepository.findAll(pageable);
        return contacts.map(contactMapper::toDTO);
    }

    @Operation(summary = "Buscar contato por ID", description = "Recupera um contato específico pelo seu identificador único")
    @GetMapping("{id}")
    public ContactDTO getContactById(@PathVariable Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + id));
        return contactMapper.toDTO(contact);
    }

    @Operation(summary = "Criar novo contato", description = "Cria um novo contato no sistema")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO createContact(@Valid @RequestBody ContactDTO contactDTO) {
        Contact contact = contactMapper.toEntity(contactDTO);
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toDTO(savedContact);
    }

    @Operation(summary = "Atualizar contato", description = "Atualiza todos os dados de um contato existente")
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

    @Operation(summary = "Atualizar contato parcialmente", description = "Atualiza apenas campos específicos de um contato existente")
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

    @Operation(summary = "Excluir contato", description = "Remove um contato do sistema")
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }

    @Operation(summary = "Buscar contatos por nome", description = "Busca contatos que contenham o nome especificado")
    @GetMapping("/search")
    public Page<ContactDTO> searchContactsByName(@RequestParam String name, 
                                                @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        Page<Contact> contacts = contactRepository.findByNomeContainingIgnoreCase(name, pageable);
        return contacts.map(contactMapper::toDTO);
    }
}