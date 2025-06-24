package output.example.contact_app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import output.example.contact_app.data.ContactLog;
import output.example.contact_app.repository.ContactRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository; // 変更

    public ContactService(ContactRepository contactRepository) { // 変更
        this.contactRepository = contactRepository;
    }

    @Transactional
    public void addContactRecord(LocalDate contactDate) {
        if (contactRepository.findByContactDate(contactDate) == null) { // 変更
            ContactLog newLog = new ContactLog(); // 変更
            newLog.setContactDate(contactDate);
            contactRepository.insert(newLog); // 変更
        } else {
            throw new IllegalArgumentException("指定された日付 (" + contactDate + ") の連絡記録は既に存在します。");
        }
    }

    public List<ContactLog> getAllContactRecords() { // 戻り値の型変更
        return contactRepository.findAllOrderByContactDateAsc(); // 変更
    }

    public ContactLog getContactRecordById(int id) { // 戻り値の型変更
        return contactRepository.findById(id); // 変更
    }

    @Transactional
    public void updateContactRecord(int id, LocalDate newContactDate) {
        ContactLog existingLog = contactRepository.findById(id); // 変更
        if (existingLog == null) {
            throw new IllegalArgumentException("ID: " + id + " の連絡記録が見つかりません。");
        }
        ContactLog logAtNewDate = contactRepository.findByContactDate(newContactDate); // 変更
        if (logAtNewDate != null && logAtNewDate.getId() != id) {
            throw new IllegalArgumentException("指定された日付 (" + newContactDate + ") には既に別の連絡記録が存在します。");
        }
        existingLog.setContactDate(newContactDate);
        contactRepository.update(existingLog); // 変更
    }

    @Transactional
    public void deleteContactRecord(int id) {
        contactRepository.delete(id); // 変更
    }

    public int calculateConsecutiveDays() {
        List<ContactLog> logs = contactRepository.findConsecutiveLog(LocalDate.now()); // 変更

        if (logs.isEmpty()) {
            return 0;
        }

        int consecutiveDays = 0;
        LocalDate expectedDate = LocalDate.now();

        if (!logs.get(0).getContactDate().equals(expectedDate) && !logs.get(0).getContactDate().equals(expectedDate.minusDays(1))) {
            return 0;
        }

        if (logs.get(0).getContactDate().equals(expectedDate)) {
            consecutiveDays = 1;
            expectedDate = expectedDate.minusDays(1);
        } else if (logs.get(0).getContactDate().equals(expectedDate.minusDays(1))) {
            consecutiveDays = 1;
            expectedDate = expectedDate.minusDays(2);
        } else {
            return 0;
        }

        for (int i = 1; i < logs.size(); i++) {
            if (logs.get(i).getContactDate().equals(expectedDate)) {
                consecutiveDays++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }
        return consecutiveDays;
    }
}
