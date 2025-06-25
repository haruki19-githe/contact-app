package output.example.contact_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import output.example.contact_app.data.ContactLog;
import output.example.contact_app.service.ContactService;

import java.util.List;

@RestController
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // 連続連絡日数を取得
    @GetMapping("/consecutive-days")
    public int getConsecutiveDays() {
        return contactService.calculateConsecutiveDays();
    }

    // 全ての連絡記録を取得
    @GetMapping("/ContactLogList")
    public List<ContactLog> getContactLogList() {
        return contactService.searchContactLogList();
    }


    // 特定のIDの連絡記録を取得
    @GetMapping("/ContactLog/id/{id}")
    public ContactLog getContactLogById(@PathVariable("id") int id) {
        ContactLog log = contactService.searchContactLogById(id);
        if (log == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact record not found with ID: " + id);
        }
        return log;
    }

    // 連絡記録の追加
    @PostMapping("/insertContactLog")
    public ResponseEntity<String> addContactLog(@RequestBody ContactLog contactLog) {
        try {
            contactService.addContactLog(contactLog.getContactDate());
            return ResponseEntity.ok("登録処理が成功しました。");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // 連絡記録の更新
    @PutMapping("/updateContactLog/id/{id}") // PUTリクエストで更新
    public ResponseEntity<String> updateContactLog(@PathVariable("id") int id, @RequestBody ContactLog contactLog) {
        try {
            contactService.updateContactLog(id, contactLog.getContactDate());
            return ResponseEntity.ok("更新処理が成功しました");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // 連絡記録の削除
    @DeleteMapping("/deleteContactLog/id/{id}") // DELETEリクエストで削除
    public ResponseEntity<String> deleteContactLog(@PathVariable("id") int id) {
        try {
            contactService.deleteContactLog(id);
            return ResponseEntity.ok("削除処理が成功しました。");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting contact record: " + e.getMessage());
        }
    }
}
