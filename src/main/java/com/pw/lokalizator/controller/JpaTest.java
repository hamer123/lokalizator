package com.pw.lokalizator.controller;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;



















import com.pw.lokalizator.model.entity.Avatar;
import com.pw.lokalizator.model.entity.Employee;
import com.pw.lokalizator.model.entity.Factory;
import com.pw.lokalizator.model.entity.User;
import com.pw.lokalizator.model.entity.UserSetting;
import com.pw.lokalizator.repository.AvatarRepository;
import com.pw.lokalizator.service.ImageService;
import com.pw.lokalizator.service.impl.AvatarService;

@Named
@ApplicationScoped
@Transactional
public class JpaTest implements Serializable{
	private static final long serialVersionUID = 1L;
	User user;
	
	@Inject
	EntityManager em;
	@Inject
	ImageService service;
	@EJB
	AvatarRepository avatarRepository;
	
	private File file;
	
	@PostConstruct
	public void postConstruct(){
		file = new File("C:\\Users\\Patryk\\Desktop\\Dj66ihQ.jpg");
		
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString());
	}

	public void find(){
		user = em.find(User.class, 51L);
	}
	
	@Transactional(value = Transactional.TxType.REQUIRED)
	public void merge(){
		user.setPhone("1111134234");
		user.setAvatar(null);
		em.merge(user);
	}
	
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void create(){
		UserSetting userSetting = new UserSetting();
		userSetting.setgMapZoom(1);
		userSetting.setDefaultLatitude(1);
		userSetting.setDefaultLongtitude(1);
		em.persist(userSetting);
	}
	
	public void onAction(){
		Factory factory = new Factory();
		Employee employee = new Employee();
		employee.setFactory(factory);
		Employee employee2 = new Employee();
		employee2.setFactory(factory);
		List<Employee>employees = new ArrayList<Employee>();
		employees.add(employee);
		employees.add(employee2);
		factory.setEmployees(employees);
		
		em.persist(factory);
	}
	
	public void onFind(){
		Factory factory = em.find(Factory.class, 24L);
	
//		remove(factory);
		
		List<Employee>employees = factory.getEmployees();
		employees.clear();
		
		Employee employee = new Employee();
		employee.setFactory(factory);
		Employee employee2 = new Employee();
		employee2.setFactory(factory);
		employees.add(employee);
		employees.add(employee2);
		
//		em.persist(employee);
//		em.persist(employee2);	
		//
	}
	
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void remove(Factory factory){
		factory.getEmployees().clear();
		em.flush();
	}
	
	public byte[] image() throws IOException{
		return service.content("57115554_p0.jpg");
	}
	
//	public InputStream image2() throws IOException{
//		return new FileInputStream(new File("C:\\Users\\Patryk\\Desktop\\Dj66ihQ.jpg"));
//	}
}
