package gr.aueb.cf.expensesApp.dto;

import gr.aueb.cf.expensesApp.core.enums.ErrorCode;

public class ResponseMessageDTO {
    private String code;
    private String message;

    public ResponseMessageDTO(ErrorCode code, String message) {
        this.code = code.toString();
        this.message = message;
    }

}

