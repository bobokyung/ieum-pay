package com.ieum.pay.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcrCount {
    @Id
    private Long memberId;
    @Setter
    private int count;
}
