package com.example.app.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @NotBlank(message = "Song name cannot be empty or null")
    private String name;

    @NotBlank(message = "Genre cannot be empty or null")
    private String genre;

    @NotBlank(message = "Singer cannot be empty or null")
    private String singer;

    @Min(value = 1, message = "Time must be greater than 0")
    private int time;

    @Min(value = 1900, message = "Release year must be after 1900")
    private int releaseYear;

    @Min(value = 1, message = "Popularity score must be greater than 0")
    @Max(value = 100, message = "Popularity score must be less than or equal to 100")
    private int popularityScore;

}
