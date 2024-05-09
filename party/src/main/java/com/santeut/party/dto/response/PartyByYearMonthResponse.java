package com.santeut.party.dto.response;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PartyByYearMonthResponse {
  public Set<String> date;
}
