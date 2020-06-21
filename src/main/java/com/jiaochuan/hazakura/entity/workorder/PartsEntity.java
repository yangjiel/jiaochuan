package com.jiaochuan.hazakura.entity.workorder;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="auth_user")
@Entity
@Data
@RequiredArgsConstructor
public class PartsEntity {
    @Id
    @Column(name = "id", columnDefinition = "INTEGER")
    public Integer id;

    @Column(name = "name", length = 16)
    @NonNull
    public String name;

    @Column(name = "model", length = 16)
    @NonNull
    public String model;

    @Column(name = "manufacture", length = 16)
    public String manufacture;


}
