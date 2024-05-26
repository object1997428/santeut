package com.santeut.party.dto.response;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartyByYearMonthResponse {
  public Set<String> date;
}
