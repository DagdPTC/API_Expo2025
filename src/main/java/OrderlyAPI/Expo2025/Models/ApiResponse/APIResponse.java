package OrderlyAPI.Expo2025.Models.ApiResponse;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class APIResponse<R> {
    private boolean success;
    private String message;
    private R data;

    public APIResponse(boolean success, String message, R data){
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
