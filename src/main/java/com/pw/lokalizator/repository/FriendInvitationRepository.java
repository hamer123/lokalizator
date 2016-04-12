package com.pw.lokalizator.repository;

import java.util.List;

import javax.ejb.Local;

import com.pw.lokalizator.model.FriendInvitation;

@Local
public interface FriendInvitationRepository extends JpaRepository<FriendInvitation, Long> {
	FriendInvitation getByUsersId(long nadawcaID , long odbiorcaID);
	List<FriendInvitation> getByOdbiorcaId(long id);
	int remove(long senderId, long reciverId);
}
