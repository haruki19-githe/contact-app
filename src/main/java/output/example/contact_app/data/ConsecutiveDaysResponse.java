package output.example.contact_app.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsecutiveDaysResponse {
    private int consecutiveDays;
    private String message;
}