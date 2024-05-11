package com.santeut.party.repository;

import com.santeut.party.dto.chatting.ChatMessage;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {

  @Aggregation(pipeline = {
      "{'$match':  {'partyId': ?0}}",
      "{'$sort':  {'createdAt':-1}}",
      "{'$limit': 1}"
  })
  Optional<ChatMessage> findTop1ByPartyIdOrderByCreatedAtDesc(int partyId);

}
