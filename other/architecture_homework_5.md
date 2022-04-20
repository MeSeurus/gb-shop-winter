# Домашнее задание №5

Для проекта был реализован listener (https://github.com/MeSeurus/client-api/pull/2) :
```java
public class OrderListener {

    private MailService mailService;

    @JmsListener(destination = JmsConfig.ORDER_CHANGED)
    public void listen(@Payload OrderEvent orderEvent){
        System.out.println(orderEvent);
        mailService.sendMail("redoreole@gmail.com",
                "Order change", orderEvent.getOrderDto().toString());
    }
}
```
Который работал с очередью ActiveMQ. Прослушиваемый сервис отправки сообщений:

```java
@Component
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String to, String subject, String text){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("gb-client-api");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }
}
```

Конфиг для этого выглядит следующим образом:

```java
@Configuration
public class JmsConfig {

    public static final String ORDER_CHANGED = "order-changed";

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
```

Спасибо!