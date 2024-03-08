package org.example.entity;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
public class CSV {
    private String firstName;
    private String lastName;
    private String address;
    private String specificAdd;
    private String city;
    private String postCode;
}
