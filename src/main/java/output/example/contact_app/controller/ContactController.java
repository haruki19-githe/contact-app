package output.example.contact_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import output.example.contact_app.data.ContactLog;
import output.example.contact_app.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contact-logs")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // 連続連絡日数を取得
    @GetMapping("/consecutive-days")
    public ResponseEntity<Integer> getConsecutiveDays() {
        int consecutiveDays = contactService.calculateConsecutiveDays();
        return ResponseEntity.ok(consecutiveDays); // HTTP 200 OK
    }

    // 全ての連絡記録を取得
    @GetMapping
    public ResponseEntity<List<ContactLog>> getAllContactLogs() { // 戻り値を List<ContactLog> に変更
        List<ContactLog> logs = contactService.getAllContactRecords();
        return ResponseEntity.ok(logs); // DTOへの変換不要
    }


    // 特定のIDの連絡記録を取得
    @GetMapping("/{id}")
    public ResponseEntity<ContactLog> getContactLogById(@PathVariable("id") int id) {
        ContactLog log = contactService.getContactRecordById(id);
        if (log == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact record not found with ID: " + id);
        }
        return ResponseEntity.ok(log);
    }

    // 連絡記録の追加
    @PostMapping
    public ResponseEntity<Void> addContactLog(@RequestBody ContactLog contactLog) { // ContactLogを直接受け取る
        try {
            contactService.addContactRecord(contactLog.getContactDate());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // 連絡記録の更新
    @PutMapping("/{id}") // PUTリクエストで更新
    public ResponseEntity<Void> updateContactLog(@PathVariable("id") int id, @RequestBody ContactLog contactLog) { // ContactLogを直接受け取る
        try {
            contactService.updateContactRecord(id, contactLog.getContactDate());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); // 'new' が重複している
        }
    }

    // 連絡記録の削除
    @DeleteMapping("/{id}") // DELETEリクエストで削除
    public ResponseEntity<Void> deleteContactLog(@PathVariable("id") int id) {
        try {
            contactService.deleteContactRecord(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting contact record: " + e.getMessage());
        }
    }
}
