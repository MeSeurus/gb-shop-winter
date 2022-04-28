# Домашнее задание №7

В качестве реализации паттерна MVC рассмотрим взаимодействие с корзиной (Cart) в проекте.
Первый уровень или бизнес-прослойка (Model) представляет собой сущность из БД:

Сама сущность:
```java
@Component
@Getter
@Setter
@AllArgsConstructor
public class Cart {
    private Integer id;
    private Map<ProductDto, Integer> products;

    public Cart (){
        id = new Random().nextInt(1000);
        products = new ConcurrentHashMap<>();
    }
}
```

View прослойка реализована с помощью фреймворка ThymeLeaf:

```html
<!DOCTYPE html>
<html lang="en" xmlns:sec="http://www.w3.org/1999/xhtml">
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"></script>
</head>
<body>

<div th:replace="~{common/navbar :: navi(Cart)}"/>

<div class="container">
    <span th:if="${products.size() == 0}">Empty</span>
    <div th:unless="${products.size() == 0}">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Name</th>
                <th scope="col"></th>
                <th scope="col">Price</th>
                <th scope="col">Quantity</th>
                <th scope="col">Remove</th>
            </tr>
            </thead>
            <tbody>
            <tr th:each="product, i : ${products.keySet()}" th:attr="prod_index=${product.id}">
                <th scope="row" th:text="${i.count}"></th>
                <td th:text="${product.title}"></td>
                <td>
                    <div>
                        <img th:src="@{'/product/images/' + ${product.id}}" alt="" width="50px" height="50px"/>
                    </div>
                </td>
                <td th:text="${product.cost}"></td>
                <td th:text="${products.get(product)}"></td>
                <div>
                    <td>
                        <a type="button" th:href="@{/cart/delete(id=${product.id})}"
                           class="btn btn-danger">Remove</a>
                    </td>
                </div>
                <div sec:authorize="hasAuthority('product.read') || isAnonymous()">
                    <td>
                        <a class="btn btn-warning" th:href="@{'/product/' + ${product.id}}"
                           role="button">Check</a>
                    </td>
                </div>
            </tr>
            </tbody>
        </table>
        <hr>
    </div>
</div>

<div th:replace="~{common/footer :: footerBlock}"/>
</body>
</html>
```
Реализация контроллера (Controller) представлена далее:

```java
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final Cart cart = new Cart();

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("products", cart.getProducts());
        return "cart/cart";
    }

    @GetMapping("/add/{productId}")
    public String addProductToCart(Model model, @PathVariable(name = "productId") Long id) {
        cartService.addProductToCart(cart, id);
        model.addAttribute("products", cart.getProducts());
        return "cart/cart";
    }

    @GetMapping("/delete")
    public String deleteProductFromCart(@RequestParam(name = "id") Long id) {
        cartService.deleteProductFromCart(cart, id);
        return "redirect:/cart";
    }
}
```
Также планируется добавить спецификацию Swagger.

Спасибо!