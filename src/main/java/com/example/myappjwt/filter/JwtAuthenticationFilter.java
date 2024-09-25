package com.example.myappjwt.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.myappjwt.db_hungha2.mapper.UserMapper;
import com.example.myappjwt.db_hungha2.model.User;
import com.example.myappjwt.db_hungha2.model.UserExample;
import com.example.myappjwt.security.JwtProvider;
import com.example.myappjwt.service.impl.CustomUserDetailsService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = header.substring(7);
        try {
            String username = jwtProvider.validateAccessToken(accessToken);
            if (username != null) {
                UserExample userExample = new UserExample();
                userExample.createCriteria().andUsernameEqualTo(username);
                List<User> users = userMapper.selectByExample(userExample);

                if (!users.isEmpty()) {
                    User user = users.get(0);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
        filterChain.doFilter(request, response);
    }
}