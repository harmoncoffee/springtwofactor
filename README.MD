# Spring: Authentication w/ Two Factor

### Description

Hello, this project is used to show an example for two-factor authentication. 
In this example we're using a basic authentication + a Time-based One Time Password (totp) token that 
will work using the Google authenticator app.

### Example Webpage: 

You can view this example by navigating to: http://springtwofactor.pharmondev.com 

This will 
redirect you to my personal server where this code is stored running. I use no-ip to keep my dynamic ip up
to date, so it should be up and running, I can make sure it is if not when viewing this, contact harmon.patrick@ymail.com

Notes: 

| Username | Password |
| -------- |----------|
| admin    | password |
| user     | password |

In the backend the passwords are saved as: {bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW

You can use these QR codes, but they're also on the login page for ease of use. 

Admin QR: 

![](https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/pharmondev:admin?secret=2222&issuer=pharmondev)  

User QR:

![](https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/pharmondev:user?secret=5555&issuer=pharmondev)

### Spring 5 vs Spring 6 changes
To support Spring 6 you will need to update the thymeleaf dependencies. And ensure that the jakarta 
HttpServletRequest/Response packages are used instead of javax

### Database Setup
For this application I used postgres. Here are the DDL's I used. 
I took this mostly from Spring Documentation I added the totp_token column.

https://docs.spring.io/spring-security/reference/5.8/servlet/authentication/passwords/jdbc.html

```postgresql
CREATE TABLE public.users (
    username varchar(50) NOT NULL,
    password varchar(500) NOT NULL,
    enabled bool NOT NULL,
    totp_token varchar NULL,
    CONSTRAINT users_pkey PRIMARY KEY (username)
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);
```

Additionally for demo purposes I added this code to initially load the tables with data. Note that I also seperately updated the users table with hardcoded secrets for the purposes of the totp token/secret. 
```java
    @Bean
    UserDetailsManager users(@Autowired DataSource dataSource) {
        UserDetails user = User.builder()
                .username("user")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles("USER", "ADMIN")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(admin);
        return users;
    }
```
