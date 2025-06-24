package output.example.contact_app.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactLog {

    private int id;
    private LocalDate contactDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
