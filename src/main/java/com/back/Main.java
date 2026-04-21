package com.back;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static class Quote {
        static int lastQuoteNo = 0;
//        int quoteNo; // list로 할 때 필요할 듯
        String quote;
        String author;
        Quote(String quote, String author) {
            this.quote = quote;
            this.author = author.isEmpty() ? "입력없음": author;
            lastQuoteNo++;
        }
        public String getQuote() { return quote;}
        public String getAuthor() { return author;}
        public void setQuote(String quote) { this.quote = quote;}
        public void setAuthor(String author) {this.author = author.isEmpty() ? "입력없음": author;}
        public boolean isEmpty() { return this.quote.isEmpty();}
        public void deleteQuote() { quote=""; author="";}
    }
    private static Quote[] quotes; // 요구조건 5단계 배열 사용

    public static void main(String[] args) {
        CmdMsg currentCmd = CmdMsg.NONE; // 입력 명령
        quotes = new Quote[16];

        Scanner sc = new Scanner(System.in);
        String input;

        System.out.print(GuideMsg.INTRO.value);

        while(true) {
            System.out.print(GuideMsg.CMD.value);
            input = sc.nextLine();

            currentCmd = CmdMsg.from(input);
            if (currentCmd == CmdMsg.NONE) {
                System.out.print(GuideMsg.ERROR.value);
            }

            switch (currentCmd) {
                case REGISTER -> register(sc);
                case VIEW -> view();
                case DELETE -> delete(CmdMsg.idx);
                case EDIT -> edit(sc, CmdMsg.idx);
                case EXIT -> System.exit(0); // 정상종료
                default -> {} // do nothing
            }
        }

    }
    // Quote[] quotes 조작
    private static void addQuote(Quote q) {
        if (Quote.lastQuoteNo >= quotes.length) {
            quotes = Arrays.copyOf(quotes, quotes.length*2);
        }
        quotes[Quote.lastQuoteNo-1] = q;
        System.out.printf(GuideMsg.REGISTER.value, Quote.lastQuoteNo);
    }

    // 기능
    private static void register(Scanner sc) {
        String quote, author;
        System.out.print(GuideMsg.QUOTE.value);
        quote = sc.nextLine();
        if (quote.isEmpty()) { System.out.print(GuideMsg.EMPTY.value); return;}
        System.out.print(GuideMsg.AUTHOR.value);
        author = sc.nextLine();
        addQuote(new Quote(quote, author));
    }
    private static void view() {
        System.out.print(GuideMsg.VIEW.value);
        for(int i=Quote.lastQuoteNo-1; i>=0; i--) {
            if (!quotes[i].isEmpty()) {
                System.out.printf("%d / %s / %s\n", i + 1, quotes[i].getAuthor(), quotes[i].getQuote());
            }
        }
    }
    private static void delete(int idx) {
        if (idx <= -1 || idx >= quotes.length || quotes[idx] == null || quotes[idx].isEmpty()) {
            System.out.printf(GuideMsg.ABSENT.value, idx+1);
            return;
        }
        quotes[idx].deleteQuote();
        System.out.printf(GuideMsg.DELETE.value, idx+1);
    }
    private static void edit(Scanner sc, int idx) {
        String quote;
        if (idx <= -1 || idx >= quotes.length || quotes[idx] == null || quotes[idx].isEmpty()) {
            System.out.printf(GuideMsg.ABSENT.value, idx+1);
            return;
        }
        System.out.print(GuideMsg.QUOTEOLD.value+quotes[idx].getQuote()+"\n"+GuideMsg.QUOTE.value);
        quote = sc.nextLine();
        if (quote.isEmpty()){ System.out.print(GuideMsg.EMPTY.value); return;}
        quotes[idx].setQuote(quote);
        System.out.print(GuideMsg.AUTHOROLD.value+quotes[idx].getAuthor()+"\n"+GuideMsg.AUTHOR.value);
        quotes[idx].setAuthor(sc.nextLine());
    }

    // 입력받을 명령어
    enum CmdMsg {
        NONE, DELETE, EDIT, EXIT, REGISTER, VIEW;
        static int idx = -1; // 다른 방법?
        public static CmdMsg from(String input) {
            // 삭제 및 수정
            if (input.length()>2) {
                if (input.length()<7) { return NONE;}
                if (input.startsWith("?id=", 2)) {
                    try {
                        idx = Integer.parseInt(input.substring(6))-1;
                    } catch(Error e) { System.out.print(GuideMsg.EMPTY.value); return NONE;}
                    if (input.startsWith("삭제")) { return DELETE;}
                    else if (input.startsWith("수정")){ return EDIT;}
                }
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

    // 출력 스트링 모음
    enum GuideMsg {
        INTRO("== 명언 앱 ==\n"),
        CMD("명령) "),
        QUOTE("명언 : "),
        AUTHOR("작가 : "),
        QUOTEOLD("명언(기존) : "),
        AUTHOROLD("작가(기존) : "),
        REGISTER("%d번 명언이 등록되었습니다.\n"),
        EMPTY("명언이 입력되지 않았습니다.\n"), // 요구사항에 없는 내용
        VIEW("""
                번호 / 작가 / 명언
                ----------------------
                """),
        DELETE("%d번 명언이 삭제되었습니다.\n"),
        ABSENT("%d번 명언은 존재하지 않습니다.\n"),

        ERROR("잘못된 명령입니다. 다시 입력해주세요. " +
                      "(예시 - 등록 / 목록 / 삭제?id=숫자 / 수정?id=숫자 / 종료)\n"); // 요구사항에 없는 내용

        final String value;
        GuideMsg(String value) {this.value = value;}
    }
}