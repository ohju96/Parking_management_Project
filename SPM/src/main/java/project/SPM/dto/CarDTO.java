package project.SPM.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO {

    private String name;
    private String phoneNumber;
    private String carNumber;
    private String address;

    public CarDTO(String name, String phoneNumber, String carNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
        this.address = address;
    }

    public CarDTO() {

    }
}
