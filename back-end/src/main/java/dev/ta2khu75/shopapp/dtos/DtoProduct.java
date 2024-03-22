package dev.ta2khu75.shopapp.dtos;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoProduct {
    @NotBlank(message = "name không được để trắng")
    @Size(min = 3, max = 200, message = "name phải hơn 2 và nhỏ hơn 200 ký tự")
    private String name;
    @Min(value = 0, message = "Price phải lớn hơn hoặc bằng 0")
    @Max(value = 10000000, message = "giá phải nhỏ hơn hoặc bằng 10.000.000")
    private float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;
    private List<MultipartFile> files;
}