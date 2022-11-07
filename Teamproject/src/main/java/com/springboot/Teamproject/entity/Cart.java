package com.springboot.Teamproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private int number;

    @OneToOne
    @JoinColumn(name = "User_id")
    private User userprofile;   //유저 정보

    @OneToMany(mappedBy = "cart")
    private List<Product> productList;    //상품 정보
}
