package com.back;

public class Quote {
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