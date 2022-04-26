# Домашнее задание №5

Согласно заданию для объектов, хранящихся в БД, юыли реализованы шаблоны Mapper'ов. 
Рассмотрим на примере Product:

Сама сущность: 
```java
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product extends InfoEntity {

    @Column(name = "title")
    private String title;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "manufacture_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"M/d/yy", "dd.MM.yyyy"})
    private LocalDate manufactureDate;
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Singular
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductImage> images;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "full_description")
    private String fullDescription;

    @Override
    public String toString() {
        return "Product{" +
                ", title='" + title + '\'' +
                ", cost=" + cost +
                ", manufactureDate=" + manufactureDate +
//                ", manufacturer=" + manufacturer.getName() +
                "}\n";
    }


    @Builder
    public Product(Long id, int version, String createdBy, LocalDateTime createdDate, String lastModifiedBy,
                   LocalDateTime lastModifiedDate, String title, BigDecimal cost, LocalDate manufactureDate,
                   Manufacturer manufacturer, Set<Category> categories, Status status) {
        super(id, version, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.title = title;
        this.cost = cost;
        this.manufactureDate = manufactureDate;
        this.manufacturer = manufacturer;
        this.categories = categories;
        this.status = status;
    }
}
```

Product Dto:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=6, fraction=2)
    private BigDecimal cost;
    @PastOrPresent
    @JsonFormat(pattern="dd.MM.yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate manufactureDate;
    @NotNull
    private Status status;
    private String manufacturer;
    private Set<CategoryDto> categories;
}
```
ProductMapper, который работает с ProductDto:

```java
@Mapper(uses = {ManufacturerMapper.class, CategoryMapper.class})
public interface ProductMapper {
    Product toProduct(ProductDto productDto, @Context ManufacturerDao manufacturerDao, @Context CategoryDao categoryDao);

    ProductDto toProductDto(Product product);

    @Mapping(source = "manufacturer", target = "manufacturerDto")
    ProductManufacturerDto toProductManufacturerDto(Product product);

    default Manufacturer getManufacturer(String manufacturer, @Context ManufacturerDao manufacturerDao) {
        return manufacturerDao.findByName(manufacturer).orElseThrow(
                () -> new NoSuchElementException("There isn't manufacturer with name" + manufacturer)
        );
    }

    default String getManufacturer(Manufacturer manufacturer) {
        return manufacturer.getName();
    }

    default Set<Category> categorySetStringToSetCategory(Set<String> categories, @Context CategoryDao categoryDao){
        return categories.stream().map(c -> categoryDao.findByTitle(c).orElseThrow(
                        () -> new NoSuchElementException("There isn't category with name" + c)
                ))
                .collect(Collectors.toSet());
    }
    default Set<String> categorySetCategoryToSetString(Set<Category> categories){
        return categories.stream().map(Category::getTitle).collect(Collectors.toSet());
    }
}
```

Проверка (типа Identity map) может производиться в Product сервисе:

```java

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductDao productDao;
    private final ManufacturerDao manufacturerDao;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        return productMapper.toProductDto(productDao.findById(id).orElse(null));
    }
 // Прочие функции сервиса...
}
```
Спасибо!