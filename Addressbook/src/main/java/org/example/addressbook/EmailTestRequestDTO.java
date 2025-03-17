package org.example.addressbook;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTestRequestDTO {
    private String email;
    private String username; // Optional; you can set a default if needed
}
