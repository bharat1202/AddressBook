package org.example.addressbook;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll()
                .stream()
                .map(contact -> modelMapper.map(contact, ContactDTO.class))
                .collect(Collectors.toList());
    }

    public ContactDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        return modelMapper.map(contact, ContactDTO.class);
    }

    public ContactDTO addContact(ContactDTO contactDTO) {
        Contact contact = modelMapper.map(contactDTO, Contact.class);
        Contact savedContact = contactRepository.save(contact);
        return modelMapper.map(savedContact, ContactDTO.class);
    }

    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setAddress(contactDTO.getAddress());
        contact.setPhoneNumber(contactDTO.getPhoneNumber());

        Contact updatedContact = contactRepository.save(contact);
        return modelMapper.map(updatedContact, ContactDTO.class);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}