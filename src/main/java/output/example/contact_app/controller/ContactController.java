package output.example.contact_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import output.example.contact_app.data.ConsecutiveDaysResponse;
import output.example.contact_app.data.ContactLog;
import output.example.contact_app.service.ContactService;

import java.util.List;

@RestController
public class ContactController {

    private ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    /**
     * 現在の連絡継続日数を取得し、それに応じたメッセージを返します。
     *
     * @return 連絡継続日数と励ましメッセージを含むレスポンス
     */
    @GetMapping("/consecutive-days")
    public ResponseEntity<ConsecutiveDaysResponse> getConsecutiveDays() {
        int consecutiveDays = contactService.calculateConsecutiveDays();
        String message = ContactService.generateConsecutiveDaysMessage(consecutiveDays);

        ConsecutiveDaysResponse response = new ConsecutiveDaysResponse(consecutiveDays, message);
        return ResponseEntity.ok(response);
    }

    /**
     * 全ての連絡記録を取得します
     *
     * @return 連絡記録のリスト
     */
    @GetMapping("/ContactLogList")
    public List<ContactLog> getContactLogList() {
        return contactService.searchContactLogList();
    }

    /**
     * 指定されたIDの連絡記録を取得します
     *
     * @param id 連絡記録のID
     * @return 対応する連絡記録
     */
    @GetMapping("/ContactLog/id/{id}")
    public ContactLog getContactLogById(@PathVariable("id") int id) {
        return contactService.searchContactLogById(id);
    }

    /**
     * 指定された名前（恋人）の連絡記録を取得します
     *
     * @param lover 恋人の名前
     * @return 対応する連絡記録
     */
    @GetMapping("/ContactLog/lover/{lover}")
    public List<ContactLog> getContactLogByLover(@PathVariable("lover") String lover) {
        return contactService.searchContactLogByLover(lover);
    }

    /**
     * 新しい連絡記録を追加します
     *
     * @param contactLog 追加する連絡記録の内容
     * @return 成功メッセージ
     */
    @PostMapping("/insertContactLog")
    public ResponseEntity<String> addContactLog(@RequestBody ContactLog contactLog) {
        contactService.insertContactLog(contactLog.getLover(), contactLog.getContactDate());
        return ResponseEntity.ok("登録処理が成功しました。");
    }

    /**
     * 指定されたIDの連絡記録を更新します
     *
     * @param id         更新対象の連絡記録ID
     * @param contactLog 更新後の連絡記録データ
     * @return 成功メッセージ
     */
    @PutMapping("/updateContactLog/id/{id}")
    public ResponseEntity<String> updateContactLog(@PathVariable("id") int id, @RequestBody ContactLog contactLog) {
        contactService.updateContactLog(id, contactLog.getLover(), contactLog.getContactDate());
        return ResponseEntity.ok("更新処理が成功しました");
    }

    /**
     * 指定されたIDの連絡記録を削除します。
     *
     * @param id 削除する連絡記録のID
     * @return 成功メッセージ
     */
    @DeleteMapping("/deleteContactLog/id/{id}")
    public ResponseEntity<String> deleteContactLog(@PathVariable("id") int id) {
        contactService.deleteContactLog(id);
        return ResponseEntity.ok("削除処理が成功しました。");
    }


}