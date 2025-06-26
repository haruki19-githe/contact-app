package output.example.contact_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import output.example.contact_app.data.ContactLog;
import output.example.contact_app.repository.ContactRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    //連絡日数を計算
    public int calculateConsecutiveDays() {
        List<ContactLog> logs = contactRepository.searchConsecutiveLog(LocalDate.now());
        if (logs.isEmpty()) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        LocalDate firstDate = logs.get(0).getContactDate();
        if (!firstDate.equals(today) && !firstDate.equals(today.minusDays(1))) {
            return 0;
        }

        int consecutiveDays = 1;
        LocalDate expectedDate = firstDate.minusDays(1);

        for (int dayindex = 1; dayindex < logs.size(); dayindex++) {
            LocalDate logDate = logs.get(dayindex).getContactDate();

            if (logDate.equals(expectedDate)) {
                consecutiveDays++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }
        return consecutiveDays;
    }

    //連絡記録の全件検索
    public List<ContactLog> searchContactLogList() {
        return contactRepository.searchAllOrderByContactDateAsc(); // 変更
    }

    //ID検索
    public ContactLog searchContactLogById(int id) {
        ContactLog log = contactRepository.searchContactLogById(id);
        if (log == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact record not found with ID: " + id);
        }
        return log;
    }

    //名前検索
    public ContactLog searchContactLogByLover(String lover) {
        ContactLog log = contactRepository.searchContactLogByLover(lover);
        if (log == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact record not found with lover: " + lover);
        }
        return log;
    }

    //連絡記録の追加
    @Transactional
    public void InsertContactLog(String lover, LocalDate contactDate) {
        if (contactRepository.searchByContactDate(contactDate) == null) {
            ContactLog newLog = new ContactLog();
            newLog.setLover(lover);
            newLog.setContactDate(contactDate);
            contactRepository.insertContactLog(newLog);
        } else {
            throw new IllegalArgumentException("指定された日付 (" + contactDate + ") の連絡記録は既に存在します。");
        }
    }

    //連絡記録の更新
    @Transactional
    public void updateContactLog(int id, String lover, LocalDate newContactDate) {
        ContactLog existingLog = contactRepository.searchContactLogById(id);
        if (existingLog == null) {
            throw new IllegalArgumentException("ID: " + id + " の連絡記録が見つかりません。");
        }
        ContactLog logAtNewDate = contactRepository.searchByContactDate(newContactDate); // 変更
        if (logAtNewDate != null && logAtNewDate.getId() != id) {
            throw new IllegalArgumentException("指定された日付 (" + newContactDate + ") には既に別の連絡記録が存在します。");
        }
        existingLog.setContactDate(newContactDate);
        existingLog.setLover(lover);
        contactRepository.updateContactLog(existingLog); // 変更
    }

    //削除処理
    @Transactional
    public void deleteContactLog(int id) {
        contactRepository.deleteContactLog(id);
    }


}
