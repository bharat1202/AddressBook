package org.example.addressbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/addressbook")
public class ContactController {

    @Autowired
    private IAddressBookService addressBookService;

    // ✅ Fetch all contacts
    @GetMapping
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> getAllContacts() {
        List<ContactDTO> contacts = addressBookService.getAllContacts();
        return ResponseEntity.ok(new ResponseDTO<>("Contacts fetched successfully", contacts));
    }

    // ✅ Fetch contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ContactDTO>> getContactById(@PathVariable Long id) {
        ContactDTO contact = addressBookService.getContactById(id);
        return ResponseEntity.ok(new ResponseDTO<>("Contact found", contact));
    }

    // ✅ Add a new contact (with validation)
    @PostMapping
    public ResponseEntity<ResponseDTO<ContactDTO>> addContact(@Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO savedContact = addressBookService.addContact(contactDTO);
        return ResponseEntity.ok(new ResponseDTO<>("Contact added successfully", savedContact));
    }

    // ✅ Update an existing contact
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ContactDTO>> updateContact(@PathVariable Long id, @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO updatedContact = addressBookService.updateContact(id, contactDTO);
        return ResponseEntity.ok(new ResponseDTO<>("Contact updated successfully", updatedContact));
    }

    // ✅ Delete a contact
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteContact(@PathVariable Long id) {
        addressBookService.deleteContact(id);
        return ResponseEntity.ok(new ResponseDTO<>("Contact deleted successfully", "Deleted ID: " + id));
    }
}
