package com.pw.lokalizator.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.pw.lokalizator.model.entity.User;

@Named
@ConversationScoped
public class IndexController implements Serializable{
	private static final long serialVersionUID = -430267591398275516L;
	@Inject
	private Conversation conversation; 
	 
	private Stage stage;
	private boolean passPersonalDefault;
	private boolean passDefaultSetting;
	private User user;
	
	@PostConstruct
	public void init(){
		System.out.println("@PostConstruct");
	}
	
	public void onPersonalDataEnter(){

	}
	
	public void onDefaultSettingEnter(){
		
	}
	
	public void onFinalizeEnter(){
		
	}
	
	public void beginConversation(){
		if(!FacesContext.getCurrentInstance().isPostback() && conversation.isTransient())
			conversation.begin();
	}
	
	public void endConversation(){
		conversation.end();
	}

	public static enum Stage{
		PERSONALS,DEFAULT_SETTINGS,FINALIZE
	}
}
