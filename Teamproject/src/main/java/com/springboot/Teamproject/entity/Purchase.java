package com.springboot.Teamproject.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Data
public class Purchase {

    @Id
    private Long purchaseNumber;        //주문 번호

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;            //상품 정보

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userprofile;           //유저 정보

    private String date;                //구매 날짜
}
