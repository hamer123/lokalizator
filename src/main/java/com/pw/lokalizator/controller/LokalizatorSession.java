package com.pw.lokalizator.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.pw.lokalizator.model.User;

@Named
@SessionScoped
public class LokalizatorSession {

	private boolean isLogged;
	private User user;
}
