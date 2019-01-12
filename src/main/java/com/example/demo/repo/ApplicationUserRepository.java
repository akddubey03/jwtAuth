package com.example.demo.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.model.User;

@Component
public class ApplicationUserRepository {
	
	private static List<User> list=new ArrayList<User>();
	
	public void save(User user){
		list.add(user);
		System.out.println(list);
	}
	
	public User findByUsername(String username){
		System.out.println("finduserbyname called");
		Optional<User> opt=list.stream().filter(obj -> obj.getUsername().equals(username)).findFirst();
		System.out.println(opt.get());
		if(opt.isPresent()){
			return opt.get();
		}else{
			return null;
		}
	}

}
