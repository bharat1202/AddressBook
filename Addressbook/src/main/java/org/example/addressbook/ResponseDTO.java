package org.example.addressbook;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    private String message;
    private T data;

    // Additional constructor for message-only responses
    public ResponseDTO(String message) {
        this.message = message;
    }
}
