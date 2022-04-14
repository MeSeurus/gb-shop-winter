# Домашнее задание №3

В качестве домашнего задания выбрано указать реализацию порождающего builder-паттерна на примере класса Order:
```java
    @Builder
    public Order(Long id, int version, String createdBy, LocalDateTime createdDate, String lastModifiedBy,
                 LocalDateTime lastModifiedDate, String firstname, String lastname, String phone, String mail,
                 OrderStatus status, LocalDate deliveryDate, AccountUser accountUser, List<OrderItem> orderItems,
                 BigDecimal price, BigDecimal deliveryPrice, Address deliveryAddress, Set<Product> products) {
        super(id, version, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.mail = mail;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.accountUser = accountUser;
        this.orderItems = orderItems;
        this.price = price;
        this.deliveryPrice = deliveryPrice;
        this.deliveryAddress = deliveryAddress;
        this.products = products;
    }
```

Паттерн необходим нам для работы с сущностями используемой базы данных.
Впоследствии возможности @Builder'a широко используются, например, в функции save класса OrderService:

```java
    @Transactional
    public OrderDto save(final OrderDto orderDto) {
        Order order = orderMapper.toOrder(orderDto, manufacturerDao, categoryDao);
        if (order.getId() != null) {
            orderDao.findById(orderDto.getId()).ifPresent(
                    (p) -> order.setVersion(p.getVersion())
            );
        } else {
            order.setStatus(OrderStatus.CREATED);
        }
        OrderDto savedOrderDto = orderMapper.toOrderDto(orderDao.save(order));
        jmsTemplate.convertAndSend(JmsConfig.ORDER_CHANGED, new OrderEvent(savedOrderDto));
        return savedOrderDto;
    }
```
а также в mapper'e, OrderItem и других классах.