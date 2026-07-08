package com.javanauta.agendadortarefas.infrastructure.security;

import com.javanauta.agendadortarefas.business.dto.UsuarioDTO;
import com.javanauta.agendadortarefas.infrastructure.security.client.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioClient client;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("Use o método carregaDadosUsuario passando o Token.");
    }

    public UserDetails carregaDadosUsuario(String email, String token){
        // Concatena "Bearer " para que o microsserviço da porta 8080 valide a requisição do Feign com sucesso
        UsuarioDTO usuarioDTO = client.buscaUsuarioPorEmail(email, "Bearer " + token);

        if (usuarioDTO == null) {
            throw new UsernameNotFoundException("Usuário não encontrado no sistema remoto.");
        }

        return User
                .withUsername(usuarioDTO.getEmail())
                .password(usuarioDTO.getSenha())
                .authorities("ROLE_USER") // Define autoridade obrigatória para evitar o Erro 403 no Spring Security 6
                .build();
    }
}