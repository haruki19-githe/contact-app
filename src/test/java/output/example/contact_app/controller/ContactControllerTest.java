package output.example.contact_app.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import output.example.contact_app.data.ContactLog;
import output.example.contact_app.service.ContactService;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService service;



    @Test
    void 連絡継続日数が0日の時に警告メッセージが返されること() throws Exception {
        when(service.calculateConsecutiveDays()).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/consecutive-days"))
                .andExpect(jsonPath("$.consecutiveDays").value(0))
                .andExpect(jsonPath("$.message").value("今すぐ連絡を取りなさい、相手の心が離れてきますよ"));

        verify(service, times(1)).calculateConsecutiveDays();
    }

    @Test
    void 連絡記録の一覧が取得できること() throws Exception {
        List<ContactLog> logs = List.of(createContactLog(2), createContactLog(3));
        when(service.searchContactLogList()).thenReturn(logs);

        mockMvc.perform(MockMvcRequestBuilders.get("/ContactLogList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(3));

        verify(service, times(1)).searchContactLogList();
    }


    @Test
    void 指定IDの連絡記録が取得できること() throws Exception {
        ContactLog log = createContactLog(1);
        when(service.searchContactLogById(1)).thenReturn(log);

        mockMvc.perform(MockMvcRequestBuilders.get("/ContactLog/id/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(service, times(1)).searchContactLogById(1);
    }

    @Test
    void 恋人の名前で連絡記録が取得できること() throws Exception {
        List<ContactLog> logs = List.of(createContactLog(2), createContactLog(3));
        when(service.searchContactLogByLover("山田")).thenReturn(logs);

        mockMvc.perform(MockMvcRequestBuilders.get("/ContactLog/lover/{lover}", "山田"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lover").value("山田"));

        verify(service, times(1)).searchContactLogByLover("山田");
    }



    @Test
    void 連絡記録の登録が正常に行われること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/insertContactLog")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                               "lover": "山田",
                               "contactDate": "2025-06-25"
                           }
                        """)).andExpect(status().isOk());

        verify(service, times(1)).InsertContactLog("山田", LocalDate.of(2025, 6, 25));
    }

    @Test
    void 連絡記録の更新が正常に行われること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/updateContactLog/id/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                               "lover": "山田",
                               "contactDate": "2025-06-25"
                           }
                        """)).andExpect(status().isOk());

        verify(service, times(1)).updateContactLog(1, "山田", LocalDate.of(2025, 6, 25));
    }

    @Test
    void 連絡記録の削除が正常に行われること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteContactLog/id/{id}", 1))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteContactLog(1);
    }

    private static ContactLog createContactLog(int id) {
        ContactLog contactLog = new ContactLog();
        contactLog.setId(id);
        contactLog.setLover("山田");
        contactLog.setContactDate(LocalDate.of(2025, 6, 25));
        return contactLog;
    }
}
