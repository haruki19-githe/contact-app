package output.example.contact_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.example.contact_app.data.ContactLog;
import output.example.contact_app.repository.ContactRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @InjectMocks
    private ContactService sut;

    @Mock
    private ContactRepository repository;

    @Test
    void 連絡継続日数の計算＿3日連続の場合に3が返ること() {
        LocalDate today = LocalDate.now();
        List<ContactLog> logs = List.of(
                createContactLog(1, "田中", today),
                createContactLog(2, "田中", today.minusDays(1)),
                createContactLog(3, "田中", today.minusDays(2))
        );

        when(repository.searchConsecutiveLog(today)).thenReturn(logs);

        int result = sut.calculateConsecutiveDays();

        assertEquals(3, result);
    }

    @Test
    void IDで連絡記録を取得＿該当がある場合に正しく返ること() {
        ContactLog expected = createContactLog(1, "田中", LocalDate.of(2024, 6, 1));
        when(repository.searchContactLogById(1)).thenReturn(expected);

        ContactLog actual = sut.searchContactLogById(1);

        assertEquals(expected, actual);
    }

    @Test
    void IDで連絡記録を取得＿存在しない場合に例外が投げられること() {
        when(repository.searchContactLogById(999)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () ->
                sut.searchContactLogById(999)
        );

        assertTrue(exception.getMessage().contains("Contact record not found"));
    }

    @Test
    void 恋人の名前で記録を取得＿該当がある場合に正しく返ること() {
        ContactLog expected = createContactLog(1, "田中", LocalDate.of(2024, 6, 1));
        when(repository.searchContactLogByLover("田中")).thenReturn(List.of(expected));

        List<ContactLog> actual = sut.searchContactLogByLover("田中");

        assertEquals(List.of(expected), actual);
    }

    @Test
    void 新しい記録の挿入＿重複がなければ正常に追加されること() {
        LocalDate date = LocalDate.of(2024, 6, 2);
        when(repository.searchByContactDate(date)).thenReturn(null);

        sut.InsertContactLog("田中", date);

        verify(repository, times(1)).insertContactLog(any(ContactLog.class));
    }

    @Test
    void 新しい記録の挿入＿既に同じ日付がある場合に例外が投げられること() {
        LocalDate date = LocalDate.of(2024, 6, 2);
        when(repository.searchByContactDate(date)).thenReturn(createContactLog(1, "田中", date));

        assertThrows(IllegalArgumentException.class, () ->
                sut.InsertContactLog("田中", date)
        );
    }

    @Test
    void 記録の更新＿正常に更新できること() {
        LocalDate newDate = LocalDate.of(2024, 6, 5);
        ContactLog existing = createContactLog(1, "田中", LocalDate.of(2024, 6, 1));

        when(repository.searchContactLogById(1)).thenReturn(existing);
        when(repository.searchByContactDate(newDate)).thenReturn(null);

        sut.updateContactLog(1, "佐藤", newDate);

        verify(repository, times(1)).updateContactLog(existing);
        assertEquals("佐藤", existing.getLover());
        assertEquals(newDate, existing.getContactDate());
    }

    @Test
    void 記録の削除＿正常に削除処理が行われること() {
        sut.deleteContactLog(1);

        verify(repository, times(1)).deleteContactLog(1);
    }


    private static ContactLog createContactLog(int id, String lover, LocalDate date) {
        ContactLog log = new ContactLog();
        log.setId(id);
        log.setLover(lover);
        log.setContactDate(date);
        return log;
    }


}

