package engine.render.fonts;

import engine.render.textures.Texture;
import engine.utils.VFS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BitmapFont {

    private Texture[] pageImages = null;
    private int numChars, numKernings;
    private int fontSize, lineHeight;
    private float scaleW, scaleH;
    private HashMap<Integer, BitmapChar> bitmapChars = new HashMap<>();
    private HashMap<Integer, ArrayList<BitmapKerning>> kernings = new HashMap<>();
    public BitmapFont(String fontFile){
        this(VFS.getFile(fontFile));
    }

    /**
     * Loads a BitmapFont for rendering
     *
     * @param fontFile the file to
     */
    public BitmapFont(File fontFile){
        try {
            int currKernNum = 0;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fontFile));
            String line;
            while((line = bufferedReader.readLine())!=null){
                String[] parts = line.replaceAll("  ", " ").split(" ");
                if(line.startsWith("info")){
                    for(String part:parts){
                        String[] components = part.split("=");
                        if(components[0].equals("size"))
                            fontSize = Integer.parseInt(components[1]);
                    }
                }else if(line.startsWith("common")){
                    for(String part:parts){
                        String[] components = part.split("=");
                        switch (components[0]) {
                            case "pages":
                                pageImages = new Texture[Integer.parseInt(components[1])];
                                break;
                            case "scaleW":
                                scaleW = Integer.parseInt(components[1]);
                                break;
                            case "scaleH":
                                scaleH = Integer.parseInt(components[1]);
                                break;
                            case "lineHeight":
                                lineHeight = Integer.parseInt(components[1]);
                                break;
                        }
                    }
                }else if(line.startsWith("page")){
                    int pageNum = -1;
                    String file = null;
                    for(String part:parts){
                        String[] components = part.split("=");
                        if(components[0].equals("id"))
                            pageNum=Integer.parseInt(components[1]);
                        else if(components[0].equals("file"))
                            file = components[1].substring(1, components[1].length()-1);
                    }
                    pageImages[pageNum] = new Texture(VFS.getFileStringInSameFolder(fontFile, file));
                }else if(line.startsWith("chars count")){
                    for(String part:parts){
                        String[] components = part.split("=");
                        if(components[0].equals("count")){
                            numChars = Integer.parseInt(components[1]);
                        }
                    }
                }else if(line.startsWith("kernings count")){
                    for(String part:parts){
                        String[] components = part.split("=");
                        if(components[0].equals("count")){
                            numKernings = Integer.parseInt(components[1]);
                        }
                    }
                }else if(line.startsWith("char ")){
                    BitmapChar bitmapChar = new BitmapChar();
                    for(String part:parts) {
                        String[] components = part.split("=");
                        switch (components[0]) {
                            case "id":
                                bitmapChar.id = Integer.parseInt(components[1]);
                                bitmapChars.put(bitmapChar.id, bitmapChar);
                                break;
                            case "x":
                                bitmapChar.x = Integer.parseInt(components[1]) / scaleW;
                                break;
                            case "y":
                                bitmapChar.y = Integer.parseInt(components[1]) / scaleH;
                                break;
                            case "width":
                                bitmapChar.w = Integer.parseInt(components[1]) / scaleW;
                                bitmapChar.charW = Integer.parseInt(components[1]);
                                break;
                            case "height":
                                bitmapChar.h = Integer.parseInt(components[1]) / scaleH;
                                bitmapChar.charH = Integer.parseInt(components[1]);
                                break;
                            case "xoffset":
                                bitmapChar.xoff = Integer.parseInt(components[1]);
                                break;
                            case "yoffset":
                                bitmapChar.yoff = Integer.parseInt(components[1]);
                                break;
                            case "xadvance":
                                bitmapChar.xadv = Integer.parseInt(components[1]);
                                break;
                            case "page":
                                bitmapChar.page = Integer.parseInt(components[1]);
                                break;
                        }

                    }
                }else if(line.startsWith("kerning")){
                    currKernNum++;
                    int first=0, second=0, amount=0;
                    for(String part:parts){
                        String[] components = part.split("=");
                        switch (components[0]) {
                            case "first":
                                first = Integer.parseInt(components[1]);
                                break;
                            case "second":
                                second = Integer.parseInt(components[1]);
                                break;
                            case "amount":
                                amount = Integer.parseInt(components[1]);
                                break;
                        }
                    }
                    BitmapKerning kerning = new BitmapKerning();
                    kerning.amount=amount;
                    kerning.second=second;
                    if(!kernings.containsKey(first))
                        kernings.put(first, new ArrayList<>());
                    kernings.get(first).add(kerning);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the width of a String of text
     *
     * @param text The text to get the width of
     * @param fontSize The size of the font that the text will be drawn with
     * @return The width of the text in pixels
     */
    public float getWidth(String text, float fontSize){
        int pointer = 0;
        int lastAdv = 0;
        for(int c:text.chars().toArray()){
            if(!bitmapChars.containsKey(c))
                continue;
            pointer += bitmapChars.get(c).xoff;
            pointer += bitmapChars.get(c).x;
            pointer += (lastAdv=bitmapChars.get(c).xadv);
        }
        pointer -= lastAdv;
        return pointer * fontSize / this.fontSize;
    }

    /**
     * Gets the images for each page
     *
     * @return The <code>Texture[]</code> representing the page images
     */
    public Texture[] getPageImages() {
        return pageImages;
    }

    /**
     * Gets the character with a given character code
     *
     * @param character The character code of the character to retrieve
     * @return The <code>BitmapChar</code> representation of the character
     */
    public BitmapChar getChar(int character) {
        return bitmapChars.get(character);
    }

    /**
     * Getter for the font size
     *
     * @return The default font size of the <code>BitmapFont</code>
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Getter for the character height
     *
     * @return The default font height of the <code>BitmapFont</code>
     */
    public float getCharHeight() {
        return lineHeight;
    }

    /**
     * Getter for the character height of a specific font size
     *
     * @param fontSize The font size to be used for sampling
     * @return The character height for that font size
     */
    public float getCharHeight(float fontSize) {
        return lineHeight*fontSize/this.fontSize;
    }
}
