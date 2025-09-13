package dev.projects.HelloWorld.Dtos;

public record TodoResponse(Long id,
                           String title,
                           String description,
                           boolean done,
                           Long userId,
                           String userEmail) {
}
