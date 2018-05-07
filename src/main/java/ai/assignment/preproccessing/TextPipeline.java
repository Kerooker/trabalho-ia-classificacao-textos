package ai.assignment.preproccessing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextPipeline {

    //https://www.textfixer.com/tutorials/common-english-words.php
   private static Set<String> commonStopWords = new HashSet<>(Arrays.asList("'tis,'twas,a,able,about,across,after,ain't,all,almost,also,am,among,an,and,any,are,aren't,as,at,be,because,been,but,by,can,can't,cannot,could,could've,couldn't,dear,did,didn't,do,does,doesn't,don't,either,else,ever,every,for,from,get,got,had,has,hasn't,have,he,he'd,he'll,he's,her,hers,him,his,how,how'd,how'll,how's,however,i,i'd,i'll,i'm,i've,if,in,into,is,isn't,it,it's,its,just,least,let,like,likely,may,me,might,might've,mightn't,most,must,must've,mustn't,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,shan't,she,she'd,she'll,she's,should,should've,shouldn't,since,so,some,than,that,that'll,that's,the,their,them,then,there,there's,these,they,they'd,they'll,they're,they've,this,tis,to,too,twas,us,wants,was,wasn't,we,we'd,we'll,we're,were,weren't,what,what'd,what's,when,when,when'd,when'll,when's,where,where'd,where'll,where's,which,while,who,who'd,who'll,who's,whom,why,why'd,why'll,why's,will,with,won't,would,would've,wouldn't,yet,you,you'd,you'll,you're,you've,your".split(",")));

   //.,;:"'`!?/\|%+{}[]()*-$&=^_~@#
   private static Set<Character> punctuation = new HashSet<>(Arrays.asList('.',',',';',':','\"', '\'', '`', '!', '?', '/', '\\', '|', '%', '+', '{', '}', '[', ']', '(', ')', '*', '-', '$', '$', '&', '=', '^', '_', '~', '@', '#'));
   private String text;

    public TextPipeline(String text) {
        this.text = text;
    }

    public void executePipeline() {
        caseFold();
        tokenize();
        removeStopWords();
    }

    private void removeStopWords() {
        List<String> allTokens = Arrays.asList(text.split("\\|"));
        List<String> nonStopWordTokens = allTokens.stream().filter(it -> !commonStopWords.contains(it)).collect(Collectors.toList());

    }

    private void caseFold() {
        turnAllToLowerCase();
    }

    private void tokenize() {
        //Removing punctuation
        text = text
                .chars()
                .mapToObj(c -> (char) c)
                .filter(it -> !punctuation.contains(it))
                .map(String::valueOf)
                .collect(Collectors.joining(""));

        //Tokenizing by spaces
        text = text.replaceAll("\\s", "|");

    }

    private void turnAllToLowerCase() {
        text = text.toLowerCase();
    }
}