package by.artsem.druzhbahub.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString()
@EqualsAndHashCode()
@Builder
@AllArgsConstructor
@Entity
@Table(name = "profile_image")
@NoArgsConstructor
public class ProfileImage implements Image {
    @Id
    @SequenceGenerator(name = "profileImageIdSeqGen", sequenceName = "profile_image_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "profileImageIdSeqGen")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false)
    private String gcsFileName;

    @Column(nullable = false)
    private LocalDateTime uploadTime;
}
