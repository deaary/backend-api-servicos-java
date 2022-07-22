package com.soulcode.Servicos.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// abstrai o user do banco para que o securitu conheca seus dados ou conhecer o usuario melhor
public class AuthUserDetail implements UserDetails {

    private String login;
    private String password;

    public AuthUserDetail(String login, String password){
        this.login = login;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override // a contas nao expirou
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // a conta nao bloqueou
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // as credenciaria nao expiraram
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // o usuario esta habilitado
    public boolean isEnabled() {
        return true;
    }
}

//O Spring security nao se comunica diretamente com o nosso model User
//Entao devemos criar uma classe que ele conheca para fazer essa comunicacao
//Userdetails = guarda informacoes do contexto de autenticacao do usuario (autorizacoes, habilitado, etc)
