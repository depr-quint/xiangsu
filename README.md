# Pixel Font Sprite Generator
I made this font generator for a project I'm working on in **Java**. I needed a custom font I could use... So instead of trying to figure out how to make an actual font (*.ttf*), I used a sprite sheet.

**SIDE NOTE**: *I'd still like to learn how to make fonts properly. Not only w/ a tool, but also understand how it works/is done. Recommendations (books, articles, ...) welcome!*

If you have recommendations or questions, let me know. I'm pretty sure the code isn't written that well, so there is a lot of room for improvements and additional features.

When you plan to improve the project on your own, I'd like to ask to **open source** it as well (the reason I added a license). Because I would like to check it out and learn from it as well. Thanks.

**NOTE**: Work in progress!  

How to run it from the terminal:
```
javac xiangsu.Crisp.java
java xiangsu.Crisp
```

## More possible excitement:
- [ ] Generate all possible sprites (font and size)
- [ ] Fine-tune offset and width characters
- [ ] Layout

## Done!

- [x] Preview the sprite

## Using the Sprite
This is how I use(d) the generated sprite.

`fontImage`: used to store the sprite;   
`int[] offset`: position in sprite (x-axis)  
`int[] widths`: width of every character

```
public class Font {

    private Image fontImage;
    private int[] offset;
    private int[] widths;

    public Font(String path) {
        fontImage = new Image(path);
        offset = new int[256];
        widths = new int[256];

        int unicode = 0;
        for (int i = 0; i < fontImage.getW(); i++) {
            if (fontImage.getP()[i] == 0xffff0000) offset[unicode] = i;
            if (fontImage.getP()[i] == 0xffffff00) widths[unicode] = i - offset[unicode++];
        }
    }

    public Image getFontImage() {
        return fontImage;
    }

    public int[] getOffset() {
        return offset;
    }

    public int[] getWidths() {
        return widths;
    }
}
```

```
public void drawText(String text, int offX, int offY) {
        int offset = 0;
        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i);

            for (int y = 1; y < font.getFontImage().getH(); y++) {
                for (int x = 0; x < font.getWidths()[unicode]; x++) {
                    setPixel(x + offX + offset, y + offY, color);
                }
            } offset += font.getWidths()[unicode];
        }
    }
```
