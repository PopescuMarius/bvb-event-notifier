package com.scopert.bvbeventnotifier.storage.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class BvbLastProcessedNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NonNull
    private String newsTitle;

    @NonNull
    private LocalDateTime processedAt;
}
