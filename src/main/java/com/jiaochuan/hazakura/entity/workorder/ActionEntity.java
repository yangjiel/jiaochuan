package com.jiaochuan.hazakura.entity.workorder;

import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Table(name="action")
@Entity
@Data
@RequiredArgsConstructor
public class ActionEntity extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @NonNull
    public UserEntity user;

    @Column(name = "type", columnDefinition = "NVARCHAR(100)")
    @Enumerated(EnumType.STRING)
    @NonNull
    public String type;

    @Column(name = "status", columnDefinition = "BOOLEAN")
    @NonNull
    public Boolean status;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    @NonNull
    public Instant date;
}
