package com.santeut.party.repository;

import com.santeut.party.dto.chatting.ChatMessage;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {

  @Query("{'party_id':  ?0}")
  Optional<ChatMessage> findTopByPartyIdOOrderByCreated_atDesc(int partyId);

//  default Optional<Message> findMessageByPartyId(int partyId) {
//    return fin
//  }

}
