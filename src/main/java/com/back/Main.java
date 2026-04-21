package com.back;
import java.util.*;

public class Main {

    private static class Quote {
        static int lastQuoteNo = 0;
        int quoteId;
        String quote;
        String author;
        Quote(String quote, String author) {
            this.quoteId = ++lastQuoteNo;
            this.quote = quote;
            this.author = author.isEmpty() ? "입력없음": author;
        }
        public int getQuoteNo() { return quoteId;}
        public String getQuote() { return quote;}
        public String getAuthor() { return author;}
        public void setQuote(String quote) { this.quote = quote;}
        public void setAuthor(String author) {this.author = author.isEmpty() ? "입력없음": author;}
        public boolean compareNo(int i) {return (quoteId == i);}
    }
    private static LinkedList<Quote> quotes; // 리스트로 변경

    public static void main(String[] args) {
        CmdMsg currentCmd; // 입력 명령
        quotes = new LinkedList<>();

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
                case DELETE -> delete(CmdMsg.qid);
                case EDIT -> edit(sc, CmdMsg.qid);
                case EXIT -> System.exit(0); // 정상종료
                default -> {} // do nothing
            }
        }

    }

    // 기능
    private static void register(Scanner sc) {
        String quote, author;
        System.out.print(GuideMsg.QUOTE.value);
        quote = sc.nextLine();
        if (quote.isEmpty()) { System.out.print(GuideMsg.EMPTY.value); return;}
        System.out.print(GuideMsg.AUTHOR.value);
        author = sc.nextLine();
        quotes.add(new Quote(quote, author));
        System.out.printf(GuideMsg.REGISTER.value, Quote.lastQuoteNo);
    }
    private static void view() {
        System.out.print(GuideMsg.VIEW.value);
        Iterator<Quote> dit = quotes.descendingIterator();
        Quote q;
        while(dit.hasNext()) {
            q = dit.next();
            System.out.printf("%d / %s / %s\n", q.getQuoteNo(), q.getAuthor(), q.getQuote());
        }
    }
    private static void delete(int qid) {
        boolean isDeleted = quotes.removeIf(q -> q.compareNo(qid));
        if (isDeleted) {
            System.out.printf(GuideMsg.DELETE.value, qid);
        } else {
            System.out.printf(GuideMsg.ABSENT.value, qid);
        }
    }
    private static void edit(Scanner sc, int qid) {
        Quote tmpQ = null;
        for(Quote q: quotes) { if (q.compareNo(qid)) { tmpQ = q;}}
        if (tmpQ == null) { System.out.printf(GuideMsg.ABSENT.value, qid); return;}
        System.out.print(GuideMsg.QUOTEOLD.value+tmpQ.getQuote()+"\n"+GuideMsg.QUOTE.value);
        String newQ = sc.nextLine();
        if (newQ.isEmpty()){ System.out.print(GuideMsg.EMPTY.value); return;}
        tmpQ.setQuote(newQ);
        System.out.print(GuideMsg.AUTHOROLD.value+tmpQ.getAuthor()+"\n"+GuideMsg.AUTHOR.value);
        tmpQ.setAuthor(sc.nextLine());
    }

    // 입력받을 명령어
    enum CmdMsg {
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