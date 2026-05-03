package in.lokeshkaushik.authapp.dtos;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record ApiError(
        String status,
        String message,
        String path,
        OffsetDateTime timestamp
) {

    public static ApiError of(String status, String message, String path) {
        return new ApiError(status, message, path, OffsetDateTime.now(ZoneOffset.UTC));
    }
}
