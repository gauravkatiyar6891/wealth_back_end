package com.moptra.go4wealth.security;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException(username);
		return new UserPrincipal(user,user.getUserAuthorities()
				.stream()
				.map(ua->new SimpleGrantedAuthority(ua.getAuthority().getAuthorityType()))
				.collect(Collectors.toList()));
	}
	
	@Transactional
	public List<SimpleGrantedAuthority> findUserById(Integer integer){

		User user=userRepository.findById(integer).orElse(null);
		List<SimpleGrantedAuthority> authoritties=Collections.emptyList();
		if(user!=null){
			authoritties = user.getUserAuthorities()
					.stream()
					.map(ua->new SimpleGrantedAuthority(ua.getAuthority().getAuthorityType()))
					.collect(Collectors.toList());
		}
		return authoritties;

	}
	
	@Transactional
	public List<SimpleGrantedAuthority> findByUsername(String username){

		User user=userRepository.findByUsername(username);
		List<SimpleGrantedAuthority> authoritties=Collections.emptyList();
		if(user!=null){
			authoritties = user.getUserAuthorities()
					.stream()
					.map(ua->new SimpleGrantedAuthority(ua.getAuthority().getAuthorityType()))
					.collect(Collectors.toList());
		}
		return authoritties;

	}
}