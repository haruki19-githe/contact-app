package output.example.contact_app.repository;

import org.apache.ibatis.annotations.Mapper;
import output.example.contact_app.data.ContactLog;


import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ContactRepository {
    //全ての連絡記録を日付順で取得
    List<ContactLog> searchAllOrderByContactDateAsc();

    // IDで検索
    ContactLog searchContactLogById(int id);

    // 特定の日付の記録を検索
    ContactLog searchByContactDate(LocalDate contactDate);

    // 連絡記録を追加
    void insertContactLog(ContactLog log);

    // 連絡記録を更新
    void updateContactLog(ContactLog log);

    // 連絡記録を削除
    void deleteContactLog(int id);

    // 連続日数を計算するためのヘルパー
    List<ContactLog> searchConsecutiveLog(LocalDate today);
}
