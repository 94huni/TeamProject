package com.springboot.Teamproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pno;    //상품 번호

    @Column(nullable = false)
    private String name;     //상품 이름

    @Column(nullable = false)
    private int price;    //상품 가격

    @Column(nullable = false)
    private String discription;  //상품 정보

    @Column(nullable = false)
    private String imageFileName; //상품 이미지 파일이름

    @Column(nullable = false)
    private String code;    //카테고리 코드W

    @OneToOne(mappedBy = "product")
    @ToString.Exclude
    private Cart cart;      //장바구니 정보
}
