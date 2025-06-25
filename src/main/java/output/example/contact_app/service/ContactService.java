package output.example.contact_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<ContactLog> searchContactLogList() {
        return contactRepository.searchAllOrderByContactDateAsc(); // 変更
    }

    public ContactLog searchContactLogById(int id) {
        return contactRepository.searchContactLogById(id);
    }

    @Transactional
    public void addContactLog(LocalDate contactDate) {
        if (contactRepository.searchByContactDate(contactDate) == null) {
            ContactLog newLog = new ContactLog();
            newLog.setContactDate(contactDate);
            contactRepository.insertContactLog(newLog);
        } else {
            throw new IllegalArgumentException("指定された日付 (" + contactDate + ") の連絡記録は既に存在します。");
        }
    }

    @Transactional
    public void updateContactLog(int id, LocalDate newContactDate) {
        ContactLog existingLog = contactRepository.searchContactLogById(id);
        if (existingLog == null) {
            throw new IllegalArgumentException("ID: " + id + " の連絡記録が見つかりません。");
        }
        ContactLog logAtNewDate = contactRepository.searchByContactDate(newContactDate);
        if (logAtNewDate != null && logAtNewDate.getId() != id) {
            throw new IllegalArgumentException("指定された日付 (" + newContactDate + ") には既に別の連絡記録が存在します。");
        }
        existingLog.setContactDate(newContactDate);
        contactRepository.updateContactLog(existingLog);
    }

    @Transactional
    public void deleteContactLog(int id) {
        contactRepository.deleteContactLog(id);
    }

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

        for (int i = 1; i < logs.size(); i++) {
            LocalDate logDate = logs.get(i).getContactDate();

            if (logDate.equals(expectedDate)) {
                consecutiveDays++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }

        return consecutiveDays;
    }
}
