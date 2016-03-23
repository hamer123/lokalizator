package com.pw.lokalizator.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.pw.lokalizator.model.User;

@Named
@SessionScoped
public class LokalizatorSession implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private boolean isLogged;
	//private User user;
}
