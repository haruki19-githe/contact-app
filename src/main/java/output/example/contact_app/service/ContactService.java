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
    private  ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * 連絡が何日間連続しているかを計算します
     *
     * @return 連絡の継続日数（0以上）
     */
    public int calculateConsecutiveDays() {
        List<ContactLog> logList = contactRepository.searchConsecutiveLog(LocalDate.now());
        if (logList.isEmpty()) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        LocalDate latestLogDate = logList.get(0).getContactDate();
        if (!latestLogDate.equals(today) && !latestLogDate.equals(today.minusDays(1))) {
            return 0;
        }

        int consecutiveDaysCount = 1;
        LocalDate expectedPreviousDate = latestLogDate.minusDays(1);

        for (int i = 1; i < logList.size(); i++) {
            LocalDate currentLogDate = logList.get(i).getContactDate();

            if (currentLogDate.equals(expectedPreviousDate)) {
                consecutiveDaysCount++;
                expectedPreviousDate = expectedPreviousDate.minusDays(1);
            } else {
                break;
            }
        }
        return consecutiveDaysCount;
    }

    /**
     * すべての連絡記録を日付順で取得します
     *
     * @return 連絡記録のリスト
     */
    public List<ContactLog> searchContactLogList() {
        return contactRepository.searchAllOrderByContactDateAsc(); // 変更
    }

    /**
     * 指定されたIDの連絡記録を取得します
     *
     * @param id 連絡記録のID
     * @return 対応する連絡記録
     * @throws ResponseStatusException 該当する記録が存在しない場合
     */
    public ContactLog searchContactLogById(int id) {
        ContactLog log = contactRepository.searchContactLogById(id);
        if (log == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "お探しのIDは見つかりませんでした: " + id);
        }
        return log;
    }

    /**
     * 指定された恋人の名前で連絡記録を取得します
     *
     * @param lover 恋人の名前
     * @return 対応する連絡記録
     * @throws ResponseStatusException 該当する記録が存在しない場合
     */
    public List<ContactLog> searchContactLogByLover(String lover) {
        List<ContactLog> logList = contactRepository.searchContactLogByLover(lover);
        if (logList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "お探しの名前は見つかりませんでした: " + lover);
        }
        return logList;
    }

    /**
     * 新しい連絡記録を追加します
     *
     * @param lover 恋人の名前
     * @param contactDate 連絡した日付
     * @throws IllegalArgumentException 同じ日付の記録がすでに存在する場合
     */
    @Transactional
    public void insertContactLog(String lover, LocalDate contactDate) {
        if (contactRepository.searchByContactDate(contactDate) != null) {
            throw new IllegalArgumentException("指定された日付 (" + contactDate + ") の連絡記録は既に存在します。");
        }
        ContactLog newLog = new ContactLog();
        newLog.setLover(lover);
        newLog.setContactDate(contactDate);
        contactRepository.insertContactLog(newLog);
    }

    /**
     * 指定されたIDの連絡記録を更新します
     *
     * @param id 更新対象の記録ID
     * @param lover 更新後の恋人の名前
     * @param newContactDate 更新後の連絡日
     * @throws IllegalArgumentException 対象が存在しない、または日付が重複している場合
     */
    @Transactional
    public void updateContactLog(int id, String lover, LocalDate newContactDate) {
        ContactLog existingLog = contactRepository.searchContactLogById(id);
        if (existingLog == null) {
            throw new IllegalArgumentException("ID: " + id + " の連絡記録が見つかりません。");
        }
        ContactLog logAtNewDate = contactRepository.searchByContactDate(newContactDate);
        if (logAtNewDate != null && logAtNewDate.getId() != id) {
            throw new IllegalArgumentException("指定された日付 (" + newContactDate + ") には既に別の連絡記録が存在します。");
        }
        existingLog.setContactDate(newContactDate);
        existingLog.setLover(lover);
        contactRepository.updateContactLog(existingLog);
    }

    /**
     * 指定されたIDの連絡記録を削除します
     *
     * @param id 削除対象の記録ID
     */
    @Transactional
    public void deleteContactLog(int id) {
        ContactLog existingLog = contactRepository.searchContactLogById(id);
        if (existingLog == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID: " + id + " の連絡記録が見つかりません。削除できませんでした。");
        }
        contactRepository.deleteContactLog(id);
    }
    /**
     *
     * @param consecutiveDays 計算された連続日数
     * @return 連続日数に応じたメッセージ文字列
     */
    public static String generateConsecutiveDaysMessage(int consecutiveDays) {
        if (consecutiveDays == 0) {
            return "今すぐ連絡を取りなさい、相手の心が離れてきますよ";
        } else {
            return "その調子です！";
        }
    }

}
