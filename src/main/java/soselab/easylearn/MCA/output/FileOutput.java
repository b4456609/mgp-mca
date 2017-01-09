package soselab.easylearn.MCA.output;

import java.io.*;

/**
 * Created by bernie on 1/10/17.
 */
public class FileOutput {

    private final File parent;

    public FileOutput() {
        parent = new File("build/mca");
        if (!parent.mkdirs()) {
            System.err.println("Could not create parent directories ");
        }
    }

    public void writeFile(String name, String content) {

        File file;

        try {
            file = new File(parent, name);
            file.createNewFile();
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("build/mca/"+name), "utf-8"))) {

                file.createNewFile();

                writer.write(content);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
