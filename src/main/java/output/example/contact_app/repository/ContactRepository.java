package output.example.contact_app.repository;

import org.apache.ibatis.annotations.Mapper;
import output.example.contact_app.data.ContactLog;


import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ContactRepository {
    // 連絡記録を追加
    void insert(ContactLog log);

    // 特定の日付の記録を検索
    ContactLog findByContactDate(LocalDate contactDate);

    //全ての連絡記録を日付順で取得
    List<ContactLog> findAllOrderByContactDateAsc();

    // 連絡記録を更新
    void update(ContactLog log);

    // 連絡記録を削除
    void delete(int id);

    // IDで検索
    ContactLog findById(int id);

    // 全レコード数（任意）
    int countTotalRecords();

    // 連続日数を計算するためのヘルパー
    List<ContactLog> findConsecutiveLog(LocalDate today);
}
