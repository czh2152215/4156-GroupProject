package com.ase.byteAlchemists.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
@Data // 自动生成 getter, setter, equals, hashCode, toString 方法
@NoArgsConstructor // 无参构造函数
@AllArgsConstructor // 全参构造函数
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String categoryName;
}
