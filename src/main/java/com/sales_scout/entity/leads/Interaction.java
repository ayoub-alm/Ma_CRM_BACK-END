package com.sales_scout.entity.leads;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "interactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Interaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prospect_id", referencedColumnName = "id", nullable = false)
    private Prospect prospect;

    @ManyToOne
    @JoinColumn(name = "interlocutor_id", referencedColumnName = "id", nullable = true)
    private Interlocutor interlocutor;

    @Column(columnDefinition = "TEXT")
    private String report;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionSubject interactionSubject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionType interactionType;

    @ManyToOne
    @JoinColumn(name = "previous_interaction_id", referencedColumnName = "id", nullable = true)
    private Interaction previousInteraction;

    @OneToOne(mappedBy = "previousInteraction", cascade = CascadeType.ALL)
    private Interaction nextInteraction;

    @Column(nullable = true)
    private LocalDateTime planningDate;

    @Column(nullable = true)
    private String joinFilePath;

    @Column(nullable = true)
    private String address;

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false)
    private UserEntity agent;

    @ManyToOne
    @JoinColumn(name = "affected_to_id", referencedColumnName = "id", nullable = true)
    private UserEntity affectedTo;
}
