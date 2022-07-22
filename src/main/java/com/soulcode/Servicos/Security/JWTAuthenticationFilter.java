package com.soulcode.Servicos.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulcode.Servicos.Models.User;
import com.soulcode.Servicos.Util.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

// essa classe entra em açao ao chamar /login
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;

    public JWTAuthenticationFilter(AuthenticationManager manager, JWTUtils jwtUtils) {
        this.authenticationManager = manager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // tenta autenticar o usuario
        try{
            // {"login": "", "password": ""}
            // extrair informações de user da request "Bruta"
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate( // chama a autenticaçao do spring
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(),
                            user.getPassword(),
                            new ArrayList<>()
                    )
            );
        }catch (IOException io) {
            // caso o json da requisiçao nao bater com o User.class
    throw new RuntimeException((io.getMessage()));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // gerar o token e devolver para o usuario que se autenticou com sucesso
        AuthUserDetail user = (AuthUserDetail) authResult.getPrincipal();
        String token = jwtUtils.generateToken(user.getUsername());

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Acces-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        response.getWriter().write("{\"Authorization\": \"" +token + "\"}"); // escreve no body
        response.getWriter().flush(); // termina escrita
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // Customizar a resposta de erro do login que falhou
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().append(json());
        response.getWriter().flush();
    }
    String json(){ // formatar a mensagem de erro
        long date = new Date().getTime();
        return "{"
                + "\"timestamp\": "+ date + ","
                + "\"status\":401, "
                + "\"error\" : \"Nao autorizado\","
                + "\"message\" : \"Email/senha invalidos\","
                + "\"path\" : \"/login\""
                + "}";

    }
}
/**
 * FRONT MANDA {"login": "jr@gmail.com", "password": "12345"}
 * A partir do JSON -> User
 * Tenta realizar autenticação
 *      Caso dê certo:
 *          - Gera o token JWT
 *          - Retorna o token para o FRONT
 */
