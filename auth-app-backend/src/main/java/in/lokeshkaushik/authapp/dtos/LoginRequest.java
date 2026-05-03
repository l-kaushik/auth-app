package in.lokeshkaushik.authapp.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
