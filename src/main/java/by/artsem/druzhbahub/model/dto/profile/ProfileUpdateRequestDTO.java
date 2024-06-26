package by.artsem.druzhbahub.model.dto.profile;

import by.artsem.druzhbahub.model.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateRequestDTO {//TODO add images
    @NotBlank
    private String username;
    private String selfSummary;
    private Integer rate;
    private List<Profile> friends;
    private List<Like> likes;
    private List<Post> posts;
    private List<Review> reviewsFrom;
    private List<Review> reviewsTo;
}
