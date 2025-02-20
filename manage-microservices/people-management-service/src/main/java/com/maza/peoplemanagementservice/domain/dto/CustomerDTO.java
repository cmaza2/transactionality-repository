package com.maza.peoplemanagementservice.domain.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long idCustomer;
    private String name;
    private String gender;
    private int age;
    private String idCard;
    private String address;
    private String phone;
    private String password;
    private boolean status;
}
