package com.sales_scout.entity.leads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Interest;
import com.sales_scout.entity.data.*;
import com.sales_scout.enums.ActiveInactiveEnum;
import com.sales_scout.enums.ProspectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Entity
@Table(name = "customers")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String sigle;
    @Column(nullable = false)
    private Double capital;
    @Column(nullable = false)
    private String headOffice;
    @Column(nullable = false)
    private String legalRepresentative;
    private String yearOfCreation;
    private Date dateOfRegistration;
    @Column(nullable = true, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;
    private String fax;
    private String website;
    private String linkedin;
    private String whatsapp;
    @Column(nullable = false, unique = true)
    private String ice;
    @Column(nullable = false)
    private String rc;
    @Column(nullable = false, unique = true)
    private String ifm;
    @Column(nullable = false)
    private String patent;
    @Enumerated(EnumType.STRING)
     @Builder.Default
    private ActiveInactiveEnum active = ActiveInactiveEnum.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProspectStatus status = ProspectStatus.NEW;

//    @ManyToMany
//    @JoinTable(
//            name = "prospect_interested",
//            joinColumns = @JoinColumn(name = "prospect_id"),
//            inverseJoinColumns = @JoinColumn(name = "interested_id")
//    )
//    private Set<Interest> interestedEntities = new HashSet<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String businessDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "legal_status_id", nullable = true)
    private LegalStatus legalStatus;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "city_id", nullable = true)
    private City city;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = true)
    private Court court;

    @ManyToOne
    @JoinColumn(name = "company_size_id", nullable = true)
    private CompanySize companySize;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "industry_id", nullable = true)
    private Industry industry;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = true)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "proprietary_structure_id", nullable = true)
    private ProprietaryStructure proprietaryStructure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_title_id", nullable = true)
    private JobTitle reprosentaveJobTitle;

    @ManyToOne
    @JoinColumn(name = "title_id", nullable = true)
    private Title title;

    private String logo;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Interlocutor> interlocutors = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TrackingLog> trackingLogs =  new ArrayList<>();


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProspectInterest> prospectInterests =  new ArrayList<>();


}
