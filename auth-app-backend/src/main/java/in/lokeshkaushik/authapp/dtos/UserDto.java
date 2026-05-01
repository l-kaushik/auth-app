package in.lokeshkaushik.authapp.dtos;

import in.lokeshkaushik.authapp.entities.Provider;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserDto(UUID id,
                      String email,
                      String name,
                      String password,
                      String image,
                      boolean enable,
                      Instant createdAt,
                      Instant updatedAt,
                      Provider provider,
                      Set<RoleDto> roles) {
}
