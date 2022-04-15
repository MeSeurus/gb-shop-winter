# Домашнее задание №4

В домашнем задании было найдено два паттерна, являющихся структурными (фасады).
Первый представляет интерфейс UserService:
```java
public interface UserService {

    UserDto register(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(Long id);

    List<UserDto> findAll();

    AccountUser findByUsername(String username);

    void deleteById(Long id);
}
```

Для которого впоследствии навешивается (дополняется) функционал для существующих функций.

```java
@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUserDetailService implements UserDetailsService, UserService {

    private final AccountUserDao accountUserDao;
    private final AccountRoleDao accountRoleDao;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountUserDao.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Username: " + username + " not found")
        );
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (accountUserDao.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username: " + userDto.getUsername() + " already exists");
        }
        AccountUser accountUser = userMapper.toAccountUser(userDto);
        AccountRole accountRole = accountRoleDao.findByName("ROLE_USER");

        accountUser.setRoles(Set.of(accountRole));
        accountUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        accountUser.setStatus(AccountStatus.ACTIVE);

        AccountUser registeredAccountUser = accountUserDao.save(accountUser);

        log.info("user with username {} was registered successfully", registeredAccountUser.getUsername());

        return userMapper.toUserDto(registeredAccountUser);

    }

    @Override
    public UserDto update(UserDto userDto) {
        return userMapper.toUserDto(accountUserDao.save(userMapper.toAccountUser(userDto)));
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.toUserDto(accountUserDao.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User with id = " + id + " not found")
        ));
    }

    @Override
    public List<UserDto> findAll() {
        log.info("findAll users was called");
        return accountUserDao.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountUser findByUsername(String username) {
        return accountUserDao.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username = " + username + " not found")
        );
    }

    @Override
    public void deleteById(Long id) {
        accountUserDao.deleteById(id);
    }
}
```
Второй паттерн затрагивает описание сущностей и представляет собой последовательность (1) - Базовая сущность:
```java
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
}
```

Которую расширяет (2) - Инфо. сущность:

```java
public class InfoEntity extends BaseEntity {

    @Version
    @Column(name = "VERSION")
    private int version;
    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;
    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    public InfoEntity(Long id, int version, String createdBy, LocalDateTime createdDate, String lastModifiedBy,
                      LocalDateTime lastModifiedDate) {
        super(id);
        this.version = version;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }
}
```

От которой в свою очередь происходят следующие сущности, типа Product:

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

Кэширование с Redis будет введено чуть позже, после более полной реализации основных классов проекта.
