package ru.kata.spring.boot_security.demo.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 30, message = "Имя должно состоять из не менее чем 2 и не более чем 30 символов")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 30, message = "Фамилия должна состоять из не менее чем 2 и не более чем 30 символов")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Email обязателен для заполнения")
    @Email(message = "Неверный email")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Логин обязателен для заполнения")
    @Size(min = 2, max = 30, message = "Логин должен состоять из не менее чем 2 и не более чем 30 символов")
    @Column(name = "username", unique = true)
    private String username;

    @NotEmpty(message = "Пароль обязателен для заполнения")
    @Size(min = 6, max = 30, message = "Пароль должен состоять из не менее чем 6 и не более чем 30 символов")
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "user_roles_map",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {

    }

    public User(Long id, String name, String lastName, String email, String username, String password, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getRolesInfo() {
        return getRoles().stream().map(role -> role.getRole().substring(5) + " ").collect(Collectors.joining());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getLastName();
    }

}
