package com.pw.lokalizator.repository;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.pw.lokalizator.model.FriendInvitation;

@Stateless
public class FriendInvitationRepositoryImpl implements FriendInvitationRepository{
	@PersistenceContext
	EntityManager em;
	
	@Override
	public FriendInvitation add(FriendInvitation entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public FriendInvitation save(FriendInvitation entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(FriendInvitation entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FriendInvitation findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<FriendInvitation> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FriendInvitation getByUsersId(long nadawcaID, long odbiorcaID) {
		FriendInvitation invitation = null;
		try{
			invitation = em.createNamedQuery("FriendInvitation.findInvitationByUsersId", FriendInvitation.class)
				       .setParameter("id", nadawcaID)
				       .setParameter("id2", odbiorcaID)
				       .getSingleResult();
		}catch(Exception e){
			//NOTHING TO DO
		}
		return invitation;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FriendInvitation> getByOdbiorcaId(long id) {
		return em.createNamedQuery("FriendInvitation.findInvitationBySenderId", FriendInvitation.class)
				 .setParameter("id", id)
				 .getResultList();
	}

	@Override
	public int remove(long senderId, long reciverId) {
		return em.createNamedQuery("FriendInvitation.Native.removeByUsersId")
				 .setParameter("from_id", senderId)
				 .setParameter("to_id", reciverId)
				 .executeUpdate();
	}

}
