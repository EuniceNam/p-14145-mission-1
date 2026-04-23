package com.back;

// 입력받을 명령어
public enum CmdMsg {
    NONE, DELETE, EDIT, EXIT, REGISTER, VIEW;
    static int qid = -1; // 다른 방법?
    public static CmdMsg from(String input) {
        // 삭제 및 수정
        if (input.length()>=7 && input.startsWith("?id=", 2)) {
            try {
                qid = Integer.parseInt(input.substring(6));
            } catch(Error e) { return NONE;}
            if (input.startsWith("삭제")) { return DELETE;}
            else if (input.startsWith("수정")){ return EDIT;}
        }
        // 종료, 등록, 목록
        return switch (input) {
            case "종료" -> EXIT;
            case "등록" -> REGISTER;
            case "목록" -> VIEW;
            default -> NONE;
        };
    }
}