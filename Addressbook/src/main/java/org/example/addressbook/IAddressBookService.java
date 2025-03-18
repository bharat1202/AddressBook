package org.example.addressbook;

import java.util.List;

public interface IAddressBookService {
    List<ContactDTO> getAllContacts();
    ContactDTO getContactById(Long id);
    ContactDTO addContact(ContactDTO contactDTO);
    ContactDTO updateContact(Long id, ContactDTO contactDTO);
    void deleteContact(Long id);
}