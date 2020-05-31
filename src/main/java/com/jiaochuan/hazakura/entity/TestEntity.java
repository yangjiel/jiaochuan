package com.jiaochuan.hazakura.entity;

import javax.persistence.*;

@Table(name="test")
@Entity
public class TestEntity {
    @Id
    @Column(name="id")
    public int id;
}
