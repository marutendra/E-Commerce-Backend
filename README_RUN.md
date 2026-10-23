# Ready-to-run E-commerce Microservices

Four complete Maven projects. Your original files and my changes are already merged.
Every folder has its own pom.xml and main class, so each can be opened and run alone.

| Folder | Port | Main class |
|---|---|---|
| eureka-server  | 8761 | com.Learning.Eureka.EurekaServerApplication |
| seller-service | 8082 | com.Learning.Seller.SellerApplication |
| user-service   | 8081 | com.Learning.User.UserApplication |
| order-service  | 8083 | com.Learning.Order.OrderApplication |

`eureka-server` is optional: skip it if you already have your own Eureka server on 8761.

## Needs
JDK 17+, MongoDB running on localhost:27017, internet for the first Maven download.

## Run in IntelliJ (recommended)
1. File > Open > choose the `pom.xml` inside a service folder > "Open as Project". Repeat for each service
   (use "New Window" when asked), or open the main folder and add each pom.xml in the Maven tool window with "+".
2. Wait for Maven to finish importing. If prompted, enable Lombok annotation processing
   (Settings > Build > Compiler > Annotation Processors).
3. Run in this order, waiting for "Started ..." in each console:
   Eureka -> Seller -> User -> Order (right click the main class > Run).
4. Open http://localhost:8761. You should see SELLER-SERVICE, USER-SERVICE, ORDER-SERVICE as UP.

## Run from the terminal / Windows
Double click `start-all.bat` (needs `mvn` in PATH), or in four terminals run `mvn spring-boot:run`
inside each folder in the same order.

## Important if you already have data in MongoDB
Old CART documents were saved without `quantity`. Delete them once, otherwise checkout fails with
"Quantity must be greater than 0":
    mongosh
    use mydatabase
    db.CART.deleteMany({})

## Versions
Spring Boot 3.4.5, Spring Cloud 2024.0.1, Java 17. If your IDE shows a version error, change the
`<version>` in the `<parent>` and `<spring-cloud.version>` in the pom.xml to what you already use.

## Quick test
See section 8.1 of the PDF documentation (Postman steps).
