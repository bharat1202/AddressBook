package com.bridgelabz.addressbookmanagmentapp.service;

import com.bridgelabz.addressbookmanagmentapp.DTO.AddressBookDTO;
import com.bridgelabz.addressbookmanagmentapp.Exception.UserException;
import com.bridgelabz.addressbookmanagmentapp.Repository.AddressRepository;
import com.bridgelabz.addressbookmanagmentapp.model.AddressBookModel;
import com.bridgelabz.addressbookmanagmentapp.publisher.RabbitMQPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressBookServiceTest {

    @Mock
    private AddressRepository repository;

    @Mock
    private RabbitMQPublisher rabbitMQPublisher;

    @InjectMocks
    private AddressBookService addressBookService;

    private AddressBookModel sampleContact;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleContact = new AddressBookModel(1L, "John Doe", "1234567890");
    }

    // ===================== TEST GET ALL CONTACTS =====================
    @Test
    void getAllContacts_ShouldReturnContacts() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList(sampleContact));

        // Act
        List<AddressBookDTO> result = addressBookService.getAllContacts();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    // ===================== TEST SAVE CONTACT =====================
    @Test
    void saveContact_ShouldReturnSavedContact() {
        // Arrange
        AddressBookDTO newContact = new AddressBookDTO(null, "Jane Doe", "9876543210");
        AddressBookModel savedModel = new AddressBookModel(2L, "Jane Doe", "9876543210");

        when(repository.save(any(AddressBookModel.class))).thenReturn(savedModel);
        doNothing().when(rabbitMQPublisher).sendMessage(anyString(), anyString());

        // Act
        AddressBookDTO result = addressBookService.saveContact(newContact);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(rabbitMQPublisher).sendMessage(eq("contactQueue"), contains("New contact created"));
    }

    // ===================== TEST GET CONTACT BY ID =====================
    @Test
    void getContactById_ShouldReturnContact_WhenExists() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(sampleContact));

        // Act
        AddressBookDTO result = addressBookService.getContactById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getContactById_ShouldThrowException_WhenNotExists() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> addressBookService.getContactById(99L));
        assertEquals("Contact not found with ID: 99", exception.getMessage());
    }

    // ===================== TEST UPDATE CONTACT =====================
    @Test
    void updateContact_ShouldUpdateExistingContact() {
        // Arrange
        AddressBookDTO updatedDTO = new AddressBookDTO(null, "John Smith", "1234509876");
        AddressBookModel updatedModel = new AddressBookModel(1L, "John Smith", "1234509876");

        when(repository.findById(1L)).thenReturn(Optional.of(sampleContact));
        when(repository.save(any(AddressBookModel.class))).thenReturn(updatedModel);
        doNothing().when(rabbitMQPublisher).sendMessage(anyString(), anyString());

        // Act
        AddressBookDTO result = addressBookService.updateContact(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        verify(rabbitMQPublisher).sendMessage(eq("contactQueue"), contains("Contact updated"));
    }

    @Test
    void updateContact_ShouldThrowException_WhenContactNotExists() {
        // Arrange
        AddressBookDTO updatedDTO = new AddressBookDTO(null, "John Smith", "1234509876");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> addressBookService.updateContact(99L, updatedDTO));
        assertEquals("Contact not found with ID: 99", exception.getMessage());
    }

    // ===================== TEST DELETE CONTACT =====================
    @Test
    void deleteContact_ShouldDelete_WhenExists() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        doNothing().when(rabbitMQPublisher).sendMessage(anyString(), anyString());

        // Act
        boolean result = addressBookService.deleteContact(1L);

        // Assert
        assertTrue(result);
        verify(rabbitMQPublisher).sendMessage(eq("contactQueue"), contains("Contact deleted"));
    }

    @Test
    void deleteContact_ShouldThrowException_WhenNotExists() {
        // Arrange
        when(repository.existsById(99L)).thenReturn(false);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> addressBookService.deleteContact(99L));
        assertEquals("Contact not found with ID: 99", exception.getMessage());
    }
}
