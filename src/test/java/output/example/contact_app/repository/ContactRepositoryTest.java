package output.example.contact_app.repository;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import output.example.contact_app.data.ContactLog;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
public class ContactRepositoryTest {
    @Autowired
    private ContactRepository sut;

    @Test
    void 連続した連絡記録を取得できる() {
        List<ContactLog> logs = sut.searchConsecutiveLog(LocalDate.of(2025, 6, 27));

        assertThat(logs).hasSize(3);
        assertThat(logs.get(0).getContactDate()).isEqualTo(LocalDate.of(2025, 6, 27));
    }

    @Test
    void 全件の日付を取得できる() {
        List<ContactLog> result = sut.searchAllOrderByContactDateAsc();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getContactDate()).isEqualTo(LocalDate.of(2025, 6, 25));
        assertThat(result.get(1).getContactDate()).isEqualTo(LocalDate.of(2025, 6, 26));
        assertThat(result.get(2).getContactDate()).isEqualTo(LocalDate.of(2025, 6, 27));
    }

    @Test
    void IDで連絡記録を取得できる() {
        ContactLog log = sut.searchContactLogById(1);

        assertThat(log).isNotNull();
        assertThat(log.getLover()).isEqualTo("田中");
    }

    @Test
    void 恋人名で連絡記録を取得できる() {
        List<ContactLog> logs = sut.searchContactLogByLover("田中");
        assertThat(logs).hasSize(3);
    }

    @Test
    void 日付で連絡記録を取得できる() {
        ContactLog log = sut.searchByContactDate(LocalDate.of(2025, 6, 26));
        assertThat(log).isNotNull();

        assertThat(log.getLover()).isEqualTo("田中");
    }

    @Test
    void 連絡記録を追加できる() {
        ContactLog newLog = new ContactLog();
        newLog.setLover("田中");
        newLog.setContactDate(LocalDate.of(2025, 6, 28));

        sut.insertContactLog(newLog);

        ContactLog saved = sut.searchByContactDate(LocalDate.of(2025, 6, 28));
        assertThat(saved).isNotNull();
        assertThat(saved.getLover()).isEqualTo("田中");
    }

    @Test
    void 連絡記録を更新できる() {
        ContactLog log = sut.searchContactLogById(1);
        log.setLover("山田");

        sut.updateContactLog(log);

        ContactLog updated = sut.searchContactLogById(1);
        assertThat(updated.getLover()).isEqualTo("山田");
    }

    @Test
    void 連絡記録を削除できる() {
        sut.deleteContactLog(1);

        ContactLog deleted = sut.searchContactLogById(1);
        assertThat(deleted).isNull();
    }
}
