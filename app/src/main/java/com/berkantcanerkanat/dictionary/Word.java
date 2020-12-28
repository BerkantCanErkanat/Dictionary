package com.berkantcanerkanat.dictionary;

public class Word {
    public String wordNameE;
    public String wordNameT;
    public String level;
    public boolean isLearned = false;
    public boolean isFav = false;

    public Word(String wordNameE, String wordNameT, String level) {
        this.wordNameE = wordNameE;
        this.wordNameT = wordNameT;
        this.level = level;
    }
}
