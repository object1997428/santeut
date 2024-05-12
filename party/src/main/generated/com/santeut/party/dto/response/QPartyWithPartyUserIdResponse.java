package com.santeut.party.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.santeut.party.dto.response.QPartyWithPartyUserIdResponse is a Querydsl Projection type for PartyWithPartyUserIdResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPartyWithPartyUserIdResponse extends ConstructorExpression<PartyWithPartyUserIdResponse> {

    private static final long serialVersionUID = -1563384084L;

    public QPartyWithPartyUserIdResponse(com.querydsl.core.types.Expression<? extends com.santeut.party.entity.Party> party, com.querydsl.core.types.Expression<Integer> partyUserId) {
        super(PartyWithPartyUserIdResponse.class, new Class<?>[]{com.santeut.party.entity.Party.class, int.class}, party, partyUserId);
    }

}

