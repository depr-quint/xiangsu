# Sprite Generator

```
javac Crisp.java
```

```
java Crisp
```

## Using the Sprite
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