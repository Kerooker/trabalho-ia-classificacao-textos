import ai.assignment.preproccessing.TextPipeline;

public class Playground {

    public static void main(String[] args) {
        TextPipeline pipe = new TextPipeline("I read sometime in the last couple of weeks, an article which desribed how to play PC sound\n" +
                "through a soundblaster. I didn't save the article and all old articles have been purged from\n" +
                "our system here. \n" +
                "\n" +
                "Would whomever posted the article detailing where to connect the wires please re-post?\n" +
                "\n" +
                "Specifically, I need to know where to connect wires from the PC speaker to the SB card.\n" +
                "\n" +
                "Thx in Advance, Carl");
        pipe.executePipeline();
    }
}
