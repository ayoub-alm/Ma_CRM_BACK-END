package com.sales_scout.entity.EntityFilters;

import com.sales_scout.dto.response.ProspectResponseDto;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Getter
@Setter
public class InteractionFilter {
    private InteractionSubject interactionSubject;
    private InteractionType interactionType;
    private Date createdAtFrom;
    private Date createdAtTo;
    private Long id;
    private Optional<ProspectResponseDto> prospect;
    private Interlocutor interlocutor;
    private Date planningDate ;
    private String address;
    private UserEntity agent;
    private UserEntity affectedTo;
    private String report;
}
