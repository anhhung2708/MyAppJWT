package com.example.myappjwt.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.myappjwt.db_hungha2.mapper.UserMapper;
import com.example.myappjwt.db_hungha2.model.User;
import com.example.myappjwt.db_hungha2.model.UserExample;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = users.get(0);

        // Ensure authorities are not null
        Collection<? extends GrantedAuthority> authoritiesCollection = user.getAuthorities();
        List<GrantedAuthority> authorities = authoritiesCollection != null
            ? new ArrayList<>(authoritiesCollection)
            : Collections.emptyList();

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getUserpassword(),
            authorities
        );
    }
}