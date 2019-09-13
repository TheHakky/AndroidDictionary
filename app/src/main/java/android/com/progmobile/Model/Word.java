package android.com.progmobile.Model;

public class Word {

    private String mWord;
    private String mTranslate;

    public Word(String word, String translate) {
        this.mWord = word;
        this.mTranslate = translate;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getTranslate() {
        return mTranslate;
    }

    public void setTranslate(String translate) {
        mTranslate = translate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Word)
            return (this.getWord().equals(((Word)obj).getWord())
                    && this.getTranslate().equals(((Word)obj).getTranslate()));
        return false;
    }
}
