package output.example.contact_app.repository;

import org.apache.ibatis.annotations.Mapper;
import output.example.contact_app.data.ContactLog;


import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ContactRepository {
    /**
     * 指定された日付（today）を起点に、連続する連絡記録を日付の降順で取得します
     *
     * @param today 起点となる日付
     * @return 連続した日付の連絡記録リスト
     */
    List<ContactLog> searchConsecutiveLog(LocalDate today);

    /**
     * 全ての連絡記録を、日付の昇順で取得します
     *
     * @return すべての連絡記録のリスト
     */
    List<ContactLog> searchAllOrderByContactDateAsc();

    /**
     * 指定されたIDに一致する連絡記録を取得します
     *
     * @param id 検索対象の連絡記録のID
     * @return 対応する連絡記録、存在しない場合は null
     */
    ContactLog searchContactLogById(int id);

    /**
     * 指定された恋人の名前に一致する連絡記録を取得します。
     *
     * @param lover 恋人の名前
     * @return 対応する連絡記録、存在しない場合は null
     */
    List<ContactLog> searchContactLogByLover(String lover);

    /**
     * 指定された日付の連絡記録を取得します
     *
     * @param contactDate 検索対象の日付
     * @return 対応する連絡記録、存在しない場合は null
     */
    ContactLog searchByContactDate(LocalDate contactDate);

    /**
     * 新しい連絡記録を保存します
     *
     * @param log 追加する連絡記録
     */
    void insertContactLog(ContactLog log);

    /**
     * 既存の連絡記録を更新します
     *
     * @param log 更新する連絡記録
     */
    void updateContactLog(ContactLog log);

    /**
     * 指定されたIDの連絡記録を削除します
     *
     * @param id 削除する連絡記録のID
     */
    void deleteContactLog(int id);


}
