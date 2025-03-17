package org.example.addressbook;



import java.util.*;

class AddressBook<T extends Contact> {
    private Map<String, T> contacts = new HashMap<>();

    public void addContact(T contact) throws AddressBookException {
        if (contacts.containsKey(contact.getName())) {
            throw new AddressBookException("Contact with this name already exists.");
        }
        contacts.put(contact.getName(), contact);
        System.out.println("Contact added: " + contact);
    }


    public void removeContact(String name) throws AddressBookException {
        if (!contacts.containsKey(name)) {
            throw new AddressBookException("Contact not found.");
        }
        contacts.remove(name);
    }
    public void updateContact(String name, String newAddress, String newPhoneNumber) throws AddressBookException
    {
        if (!contacts.containsKey(name)) {
            throw new AddressBookException("Contact not found.");
        }

        T contact = contacts.get(name);
        contact.setAddress(newAddress);
        contact.setPhoneNumber(newPhoneNumber);

        System.out.println("Contact updated: " + contact);
    }


    public T searchByName(String name) throws AddressBookException {

        return contacts.get(name);
    }

    public List<T> searchByAddress(String address) {
        List<T> result = new ArrayList<>();
        for (T contact : contacts.values()) {
            if (contact.getAddress().equalsIgnoreCase(address)) {
                result.add(contact);
            }
        }
        return result;
    }

    public List<T> getSortedContacts() {
        List<T> sortedList = new ArrayList<>(contacts.values());
        sortedList.sort(Comparator.comparing(Contact::getName));
        return sortedList;
    }
}