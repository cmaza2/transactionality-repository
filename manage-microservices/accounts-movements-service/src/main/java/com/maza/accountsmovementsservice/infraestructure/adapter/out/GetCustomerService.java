package com.maza.accountsmovementsservice.infraestructure.adapter.out;

import com.maza.accountsmovementsservice.domain.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class GetCustomerService {
    @Value("${customerService.url}")
    private String url;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Method that call a api rest and obtain info customer
     *
     * @param id id customer
     * @return Customer information
     */
    public CustomerDTO getCustomer(String id,String token){
        String builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("id", id)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<CustomerDTO> customerDTO = restTemplate.exchange(
                builder,
                HttpMethod.GET,
                entity,
                CustomerDTO.class
        );
        if(customerDTO.getBody()!=null){
            return customerDTO.getBody();
        } else {
            throw new RuntimeException("Customer record not found:: " + id);
        }
    }
}
