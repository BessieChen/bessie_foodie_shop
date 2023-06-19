# bessie_foodie_shop


The goal of this project is to build a fully functional e-commerce website platform using a monolithic architecture design. It implements various features such as categorization, recommendations, search, shopping cart, address management, order processing, payment functionality, scheduled tasks, user center, and order and review management. The project uses Java as the backend development language, with a front-end and back-end separation development mode to facilitate layered design and modular management. At the same time, the project adopts the SpringBoot framework, combined with front-end technology to further enhance system flexibility and scalability.

### Tool
The project starts by examining the evolution of Internet system architectures and analyzing the technical stack and capabilities required for Java architects. In the project demonstration and monolithic architecture technology selection process, we use the PDMan tool for database modeling, integrating HikariCP and MyBatis technology, and using a general Mapper to write Restful-style Api interfaces. The project also provides a detailed analysis of transaction propagation and implements basic functions such as user registration and login.

### Core features
Based on the monolithic architecture, the project implements various core features of the e-commerce website, such as homepage carousel, product category display, product recommendations, and product reviews. In addition, we have implemented a search function for products, including keyword-based search and category search, to help users find the products they need more easily. The shopping cart module implements functions such as adding, deleting, modifying, and settling products, providing users with a convenient shopping experience.

Display category:

<img width="1680" alt="Screen Shot 2023-06-18 at 16 16 37" src="https://github.com/BessieChen/bessie_foodie_shop/assets/33269462/27f47ff2-fbc3-4b0a-afae-97d9f93fa7a7">



Keyword-based search:

<img width="1485" alt="Screen Shot 2023-06-18 at 17 04 57" src="https://github.com/BessieChen/bessie_foodie_shop/assets/33269462/8aa02a5c-164f-41c2-8888-089da13babcd">



Product details:

<img width="1680" alt="Screen Shot 2023-06-18 at 16 19 41" src="https://github.com/BessieChen/bessie_foodie_shop/assets/33269462/38056439-c728-4434-bc06-a3052ce1e4de">



Comments:

<img width="1680" alt="Screen Shot 2023-06-18 at 16 20 34" src="https://github.com/BessieChen/bessie_foodie_shop/assets/33269462/67b7ea74-49ea-4b7d-908f-9fc1d17656b9">


### Order processing and payment 
To meet the order processing and payment requirements of the e-commerce website, the project also develops address management, order creation, payment functionality, and scheduled tasks. Address management implements CRUD operations for shipping addresses and default address settings. Order creation involves order process sorting, order table design, and the implementation of an aggregated payment center. The payment function supports WeChat and Alipay payments and implements features such as QR code generation and merchant callback addresses. Scheduled tasks are used to close overdue unpaid orders, improving system stability and reliability.

<img width="1680" alt="Screen Shot 2023-06-18 at 16 21 34" src="https://github.com/BessieChen/bessie_foodie_shop/assets/33269462/019fff4d-5eb7-4362-840f-908af1e9a2ab">




### User information
In the user center module, the project implements query and modification of user information, using Hibernate to validate user information. In addition, we have implemented an avatar upload feature, including defining file save locations, image format restrictions, and size restrictions. Order management and review management functions include querying my orders, validating before order operations, review requirement analysis, developing a list of products to be reviewed, and developing product review functionality, providing comprehensive management of orders and reviews.

<img width="1311" alt="Screen Shot 2023-06-18 at 16 18 10" src="https://github.com/BessieChen/bessie_foodie_shop/assets/33269462/f7aac5c6-92b2-412d-81f2-b7bbbb300aa5">

<img width="1680" alt="Screen Shot 2023-06-18 at 16 19 09" src="https://github.com/BessieChen/bessie_foodie_shop/assets/33269462/4e24d4b8-703a-4dbd-8cd8-f99b80aba5cf">



### Deploy
Finally, to deploy the project to a cloud server and go live, we purchased a cloud server and installed and configured JDK, Tomcat, and MariaDB. Using SpringBoot's multi-environment deployment profile, we can switch between development and production environments. By packaging the SpringBoot project into a war file, we published the project to the cloud server. At the same time, the front-end project was also deployed to the cloud server through the corresponding configuration and publishing steps. In this way, the entire e-commerce website platform was successfully deployed and went live on the cloud server.

### Summary 
Through the practice of this project, we have built a fully functional, easy-to-maintain, and expandable e-commerce website platform. The project uses Java technology stack and monolithic architecture design to implement the core features of an e-commerce website. These features include product categorization, recommendations, search, shopping cart, address management, order processing, payment, scheduled tasks, user center, order, and review management. In addition, the project adopts a front-end and back-end separation development mode to facilitate layered design and modular management. Finally, we successfully deployed the project to a cloud server, enabling online operation and access.

In conclusion, this e-commerce website platform project fully demonstrates how to use the Java technology stack and monolithic architecture design to build a fully functional e-commerce website.
