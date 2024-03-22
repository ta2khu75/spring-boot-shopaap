package dev.ta2khu75.shopapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.javafaker.Faker;

import dev.ta2khu75.shopapp.component.LocalizationUtil;
import dev.ta2khu75.shopapp.dtos.DtoProduct;
import dev.ta2khu75.shopapp.dtos.DtoProductImage;
import dev.ta2khu75.shopapp.models.Product;
import dev.ta2khu75.shopapp.models.ProductImage;
import dev.ta2khu75.shopapp.responses.ResponseProduct;
import dev.ta2khu75.shopapp.responses.ResponseProductImage;
import dev.ta2khu75.shopapp.responses.ResponseProductList;
import dev.ta2khu75.shopapp.services.iservices.IRedisServiceProduct;
import dev.ta2khu75.shopapp.services.iservices.IServiceProduct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ControllerProduct {
    private static final Logger logger = LoggerFactory.getLogger(ControllerProduct.class);
    private final IServiceProduct serviceProduct;
    private final IRedisServiceProduct redisServiceProduct;
    private final LocalizationUtil localizationUtil;

    @GetMapping("")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int limit, @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") long category) {
        try {
            // TODO Implement Your Logic To Get Data From Service Layer Or Directly From
            // Repository Layer\
            logger.info(
                    String.format("keyword: %s, category_id: %d, page: %d, limit: %d", keyword, category, page, limit));
            PageRequest pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
            ResponseProductList responseProductList = redisServiceProduct.getAllProducts(keyword, category, pageable);
            if (responseProductList == null) {
                Page<ResponseProduct> productPage = serviceProduct.getAllProducts(pageable, keyword, category);
                responseProductList=new ResponseProductList(productPage.getContent(), productPage.getTotalPages());
                redisServiceProduct.saveAllProducts(responseProductList, keyword, category, pageable);
                return ResponseEntity.ok().body(responseProductList);
            }
            return ResponseEntity.ok().body(responseProductList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getMethodName(@RequestParam String ids) {
        List<ResponseProduct> list = serviceProduct
                .getProducByIds(Arrays.stream(ids.split(",")).map(Long::valueOf).toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable long id) {
        try {
            // TODO Implement Your Logic To Get Data From Service Layer Or Directly From
            // Repository Layer
            Product product = serviceProduct.getProductById(id);
            List<ResponseProductImage> productImages = serviceProduct.getAllImage(id);
            ResponseProduct responseProduct = new ResponseProduct(product, productImages);
            return ResponseEntity.ok().body(responseProduct);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    };

    @PostMapping(value = "/images/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postMethodName(@RequestPart("files") List<MultipartFile> files, @PathVariable long id) {
        // TODO: process POST request
        try {
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return new ResponseEntity<>("you can only upload maximum 5 images", HttpStatus.BAD_REQUEST);
            }
            Product newProduct = serviceProduct.getProductById(id);
            for (MultipartFile file : files) {
                if (file.getSize() == 0)
                    continue;
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("file must be an image");
                }
                String filename = storeFile(file);
                serviceProduct.createProductImage(
                        DtoProductImage.builder().imageUrl(filename).productId(newProduct.getId()).build());
            }
            return new ResponseEntity<>("updated " + files.size() + " files successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());
            if (urlResource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(urlResource);
            }
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(new UrlResource(Paths.get("uploads/", "notFound.jpg").toUri()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
            // TODO: handle exception
        }

    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@Valid @ModelAttribute DtoProduct dto,
            // @ModelAttribute List<MultipartFile> files,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> fieldError = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return new ResponseEntity<>(fieldError,
                        HttpStatus.BAD_REQUEST);
            }
            List<MultipartFile> files = dto.getFiles();
            files = files == null ? new ArrayList<>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return new ResponseEntity<>("you can only upload maximum 5 images", HttpStatus.BAD_REQUEST);
            }
            Product newProduct = serviceProduct.createProduct(dto);
            for (MultipartFile file : files) {
                if (file.getSize() == 0)
                    continue;
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("file must be an image");
                }
                String filename = storeFile(file);
                serviceProduct.createProductImage(
                        DtoProductImage.builder().imageUrl(filename).productId(newProduct.getId()).build());
            }
            // TODO Implement Your Logic To Save Data And Return Result Through
            // ResponseEntity
            // "Create Result" + newProduct + " and upload " + count + "files"
            return new ResponseEntity<>(newProduct, HttpStatus.OK);
        } catch (

        Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = checkFile(file);
        if (filename == null) {
            throw new IOException("Invalid image format");
        }
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir))
            Files.createDirectories(uploadDir);
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private String checkFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        // Kiểm tra phần mở rộng của tệp
        if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("png")) {
            return filename;
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @ModelAttribute DtoProduct dto,
            BindingResult result) {

        try {
            // TODO Implement Your Logic To Update Data And Return Result Through
            if (result.hasErrors()) {
                List<String> fieldError = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return new ResponseEntity<>(fieldError,
                        HttpStatus.BAD_REQUEST);
            }
            Product newProduct = serviceProduct.updateProduct(id, dto);
            List<MultipartFile> files = dto.getFiles();
            int size = files.size();
            files = files.get(0).getOriginalFilename().isBlank() ? null : files;
            if (files == null) {
                return new ResponseEntity<>("Update product success but update not upload image", HttpStatus.OK);
            }
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return new ResponseEntity<>("you can only upload maximum 5 images", HttpStatus.BAD_REQUEST);
            }
            serviceProduct.deleteProductImage(id);
            // Product newProduct=serviceProduct.updateProduct(id, dto);

            // Product newProduct = serviceProduct.createProduct(dto);
            for (MultipartFile file : files) {
                if (file.getSize() == 0)
                    continue;
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("file must be an image");
                }
                String filename = storeFile(file);
                serviceProduct.createProductImage(
                        DtoProductImage.builder().imageUrl(filename).productId(newProduct.getId()).build());
            }
            return new ResponseEntity<>("Update Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        try {
            // TODO Implement Your Logic To Destroy Data And Return Result Through
            // ResponseEntity
            serviceProduct.deleteProduct(id);
            return new ResponseEntity<>(String.format("Product with id = %d delete successfully", id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // @PostMapping("generateFakeProducts")
    public ResponseEntity<?> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 500; i++) {
            String nameProduct = faker.commerce().productName();
            if (serviceProduct.existsByName(nameProduct)) {
                continue;
            }
            DtoProduct dto = DtoProduct.builder()
                    .name(nameProduct)
                    .price(Float.parseFloat(faker.commerce().price(1000, 90_000_000)))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(2, 5))
                    .build();
            try {
                serviceProduct.createProduct(dto);

            } catch (Exception e) {
                // TODO: handle exception
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok().body("Fake Products created successfully");
    }
}
