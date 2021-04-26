package com.jiaochuan.hazakura.entity.workorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jiaochuan.hazakura.entity.AbstractEntity;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
public class ActionEntity extends AbstractEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private UserEntity user;

    @Column(name = "comment", columnDefinition = "VARCHAR(256)")
    private String comment;

    @Column(name = "date", columnDefinition = "DATETIME", nullable = false)
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
}
