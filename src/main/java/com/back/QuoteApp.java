package com.back;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class QuoteApp {
    private LinkedList<Quote> quotes;

    public void run() {
        CmdMsg currentCmd; // 입력받는 명령
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
    private void register(Scanner sc) {
        String quote, author;
        System.out.print(GuideMsg.QUOTE.value);
        quote = sc.nextLine();
        if (quote.isEmpty()) { System.out.print(GuideMsg.EMPTY.value); return;}
        System.out.print(GuideMsg.AUTHOR.value);
        author = sc.nextLine();

        quotes.add(new Quote(quote, author));
        System.out.printf(GuideMsg.REGISTER.value, Quote.lastQuoteNo);
    }
    private void view() {
        System.out.print(GuideMsg.VIEW.value);
        Iterator<Quote> dit = quotes.descendingIterator();
        Quote q;
        while(dit.hasNext()) {
            q = dit.next();
            System.out.printf("%d / %s / %s\n", q.getQuoteNo(), q.getAuthor(), q.getQuote());
        }
    }
    private void delete(int qid) {
        boolean isDeleted = quotes.removeIf(q -> q.compareNo(qid));
        if (isDeleted) {
            System.out.printf(GuideMsg.DELETE.value, qid);
        } else {
            System.out.printf(GuideMsg.ABSENT.value, qid);
        }
    }
    private void edit(Scanner sc, int qid) {
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
}
