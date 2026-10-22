package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aiap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiapVO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aiapgen")
    @SequenceGenerator(
        name = "aiapgen",
        sequenceName = "aiapseq",
        initialValue = 1000000001,
        allocationSize = 1
    )
    @Column(name = "aiapid")
    private Long id;

    @Column(name = "payment_date")
    private LocalDate date;

    @Column(name = "details", length = 500)
    private String details;

    @Column(name = "mode", length = 100)
    private String mode;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_group", length = 100)
    private String group;

    @Column(name = "sub_ledger", length = 150)
    private String subLedger;

    @Column(name = "ledger", length = 200)
    private String ledger;

    @Column(name = "remarks", length = 500)
    private String remarks;
}
