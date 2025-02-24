package com.sparta.levelup_backend.domain.bill.dto.responseDto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.gson.annotations.SerializedName;
import com.sparta.levelup_backend.utill.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BillStatusMessageDto {

    @SerializedName("id")
    private Long id;
    @SerializedName("status")
    private BillStatus status;
    @SerializedName("student_id")
    private Long tutorId;
    @SerializedName("tutor_id")
    private Long studentId;
    @SerializedName("bill_history")
    private String billHistory;

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "BillStatusMessageDto{" +
                "billId=" + id +
                ", status=" + status +
                ", tutorId=" + tutorId +
                ", studentId=" + studentId +
                ", billHistory=" + billHistory + '\'' +
                '}';
    }

    public static BillStatusMessageDto fromJson(String json) {
        Gson gson = new Gson();

        if (json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);  // 양쪽의 " 제거
            json = json.replace("\\\"", "\"");  // 내부 이스케이프 문자 제거
        }

        JsonElement element = JsonParser.parseString(json);
        return gson.fromJson(element, BillStatusMessageDto.class);
    }
}
